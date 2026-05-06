package org.tama.tamaapi.controller;

import lombok.RequiredArgsConstructor;
import org.tama.sharelib.common.dto.CustomPageRequest;
import org.tama.tamaapi.config.cache.BestItem;
import org.tama.tamaapi.config.cache.MyCacheType;
import org.tama.tamaapi.domain.item.*;
import org.tama.tamaapi.repository.item.*;
import org.tama.tamaapi.repository.item.query.dto.CategoryBestItemQueryResponse;
import org.tama.tamaapi.service.CacheService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static org.tama.tamaapi.exception.ErrorMessageUtil.*;

@RestController
@RequiredArgsConstructor
public class ItemApiController {

    private final CategoryRepository categoryRepository;
    private final CacheService cacheService;

    @GetMapping("/api/items/best")
    public List<CategoryBestItemQueryResponse> categoryBestItem(@RequestParam(required = false) Long categoryId, @ModelAttribute CustomPageRequest customPageRequest) {
        BestItem bestItem = BestItem.ALL_BEST_ITEM;

        List<Long> categoryIds = new ArrayList<>();
        if (categoryId != null) {
            Category category = categoryRepository.findWithChildrenById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_CATEGORY));
            categoryIds.add(categoryId);
            //상위 카테고리의 자식 포함
            categoryIds.addAll(category.getChildren().stream().map(Category::getId).toList());

            //카테고리 분류
             bestItem = switch (categoryId.intValue()){
                case 1 -> BestItem.OUTER_BEST_ITEM;
                case 5 -> BestItem.TOP_BEST_ITEM;
                case 11 -> BestItem.BOTTOM_BEST_ITEM;
                default -> throw new IllegalStateException("카테고리는 전체, 아우터, 상의, 하의 중 하나만 제공됩니다.");
             };
        }

        return (List<CategoryBestItemQueryResponse>) cacheService.get(MyCacheType.BEST_ITEM, bestItem.name());
    }

}
