package org.tama.tamaapi.repository.item.query;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.tama.sharelib.common.dto.CustomPageRequest;
import org.tama.sharelib.common.dto.CustomSort;
import org.tama.sharelib.common.util.UploadFile;
import org.tama.tamaapi.domain.item.ColorItemImage;
import org.tama.tamaapi.repository.item.ColorItemImageRepository;
import org.tama.tamaapi.repository.item.query.dto.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static org.tama.tamaapi.domain.item.QCategory.category;
import static org.tama.tamaapi.domain.item.QColorItem.colorItem;
import static org.tama.tamaapi.domain.item.QColorItemSizeStock.colorItemSizeStock;
import static org.tama.tamaapi.domain.item.QItem.*;
import static org.tama.tamaapi.domain.order.QOrderItem.orderItem;
import static org.springframework.util.CollectionUtils.isEmpty;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemQueryRepository {

    private final EntityManager em;
    private final ColorItemImageRepository colorItemImageRepository;
    private final JPAQueryFactory queryFactory;



    //--------------------------------------------------------------------------------------------------------------------------------------------------------
    //★카테고리 베스트 아이템 (인기 상품 조회)
    //SQL SUM 함수 때문에 느려서 캐시 사용
    //데스트 데이터라 어제꺼 없어서 전체 날짜 범위로 함
    public List<CategoryBestItemQueryResponse> findCategoryBestItemWithPaging(List<Long> categoryIds, CustomPageRequest customPageRequest) {
        List<CategoryBestItemQueryResponse> categoryBestItemQueryResponses = queryFactory.select
                        (new QCategoryBestItemQueryResponse(item.id, colorItem.id, item.name, item.originalPrice, item.nowPrice)).from(orderItem)
                .join(orderItem.colorItemSizeStock, colorItemSizeStock).join(colorItemSizeStock.colorItem, colorItem).join(colorItem.item, item)
                .where(categoryIdIn(categoryIds))
                .groupBy(colorItem.id)
                .orderBy(orderItem.count.sum().desc())
                .offset((long) (customPageRequest.getPage() - 1) *customPageRequest.getSize())
                .limit(customPageRequest.getSize())
                .fetch();

        //상품 이미지 세팅
        List<Long> colorItemIds = categoryBestItemQueryResponses.stream().map(CategoryBestItemQueryResponse::getColorItemId).toList();
        List<ColorItemImage> colorItemImages = colorItemImageRepository.findAllByColorItemIdInAndSequence(colorItemIds, 1);
        Map<Long, UploadFile> uploadFileMap = colorItemImages.stream().collect(Collectors.toMap(c -> c.getColorItem().getId(), ColorItemImage::getUploadFile));
        categoryBestItemQueryResponses.forEach(cbi -> cbi.setUploadFile(
                uploadFileMap.get(cbi.getColorItemId())
        ));

        //리뷰 정보 세팅
        List<CategoryBestItemReviewQueryDto> reviewQueryDtos = findAvgRatingsCountInColorItemId(colorItemIds);
        Map<Long, CategoryBestItemReviewQueryDto> reviewMap = reviewQueryDtos.stream()
                .collect(Collectors.toMap(CategoryBestItemReviewQueryDto::getColorItemId, Function.identity()));
        categoryBestItemQueryResponses.forEach(cbi -> {
                    CategoryBestItemReviewQueryDto reviewQueryDto = reviewMap.get(cbi.getColorItemId());
                    if (reviewQueryDto != null) {
                        cbi.setAvgRating(reviewQueryDto.getAvgRating());
                        cbi.setReviewCount(reviewQueryDto.getReviewCount());
                    }
                }
        );

        return categoryBestItemQueryResponses;
    }

    //이상 없지만, IDE 에러 없애려고 cast 적용
    private List<CategoryBestItemReviewQueryDto> findAvgRatingsCountInColorItemId(List<Long> colorItemIds) {
        String jpql = """
            select new org.tama.tamaapi.repository.item.query.dto.CategoryBestItemReviewQueryDto(
                isk.colorItem.id,
                CAST(ROUND(AVG(r.rating), 1) AS double),
                count(isk.colorItem.id)
            )
            from Review r
                join r.orderItem oi
                join oi.colorItemSizeStock isk
            where isk.id in :colorItemIds
            group by isk.colorItem.id
            """;

        TypedQuery<CategoryBestItemReviewQueryDto> query = em.createQuery(jpql, CategoryBestItemReviewQueryDto.class);
        query.setParameter("colorItemIds", colorItemIds);
        return query.getResultList();
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------------
    //queryDsl 검색 조건
    private OrderSpecifier<?> categoryItemSort(CustomSort sort) {
        Order direction = sort.getDirection().isAscending() ? Order.ASC : Order.DESC;
        return switch (sort.getProperty()) {
            //인데스 적용을 위해, 즉 coalesce 안 쓰기 위해 [price, discounted_price] -> [originalPrice, nowPrice]로 변경
            //case "price" -> new OrderSpecifier<>(direction, Expressions.numberTemplate(Integer.class, "coalesce({0}, {1})", item.discountedPrice, item.price));
            //아마도 중복 버그 생길 수 있음 (id desc 추가 필요)
            case "price" -> new OrderSpecifier<>(direction, item.nowPrice);

            //case "createdAt" -> new OrderSpecifier<>(Order.DESC, item.createdAt);
            //인데스 적용을 위해 item.id desc로 변경
            case "createdAt" -> new OrderSpecifier<>(Order.DESC, item.id);
            default -> throw new IllegalArgumentException("유효한 property가 없습니다.");
        };
    }

    private BooleanExpression categoryIdIn(List<Long> categoryIds) {
        return isEmpty(categoryIds) ? null : category.id.in(categoryIds);
    }

}
