package org.example.tamaapi.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.ColorItemImage;
import org.example.tamaapi.domain.item.ColorItemSizeStock;
import org.example.tamaapi.domain.item.Item;
import org.example.tamaapi.domain.order.OrderItem;
import org.example.tamaapi.exception.NotEnoughStockException;


import org.example.tamaapi.feignClient.item.ColorItemImageResponse;
import org.example.tamaapi.feignClient.item.ColorItemResponse;
import org.example.tamaapi.feignClient.item.ColorItemSizeStockResponse;
import org.example.tamaapi.feignClient.item.ItemSyncResponse;
import org.example.tamaapi.repository.item.ItemRepository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;
    private final ItemRepository itemRepository;


    //-------------동기화 로직----------------
    public void syncItem(ItemSyncResponse res){
        System.out.println("res.getColorItemImages = " + res.getColorItemImages());

        //db에 상품 반영
        saveItem(res.getItem().toEntity());

        List<ColorItem> colorItems = res.getColorItems().stream().map(ColorItemResponse::toEntity).toList();
        saveColorItems(colorItems);

        List<ColorItemSizeStock> colorItemSizeStocks = res.getColorItemSizeStocks().stream().map(ColorItemSizeStockResponse::toEntity).toList();
        saveColorItemSizeStocks(colorItemSizeStocks);

        List<ColorItemImage> colorItemImages = res.getColorItemImages().stream().map(ColorItemImageResponse::toEntity).toList();
        saveColorItemImages(colorItemImages);
    }

    // syncItem에서 직접 호출해서 트랜잭션 발동 하지 않음 -> 쓰기 지연
    // flush로 insert 쿼리, 바로 실행하게 함
    public void saveItem(Item item){
        em.persist(item);
        em.flush();
    }

    public void saveColorItems(List<ColorItem> colorItems) {

        jdbcTemplate.batchUpdate("INSERT INTO color_item(color_item_id, item_id, color_id) values (?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, colorItems.get(i).getId());
                ps.setLong(2, colorItems.get(i).getItem().getId());
                ps.setLong(3, colorItems.get(i).getColor().getId());
            }
            @Override
            public int getBatchSize() {
                return colorItems.size();
            }
        });
    }

    public void saveColorItemSizeStocks(List<ColorItemSizeStock> colorItemSizeStocks) {
        jdbcTemplate.batchUpdate("INSERT INTO color_item_size_stock(color_item_size_stock_id, color_item_id, size, stock) values (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, colorItemSizeStocks.get(i).getId());
                ps.setLong(2, colorItemSizeStocks.get(i).getColorItem().getId());
                ps.setString(3, colorItemSizeStocks.get(i).getSize());
                ps.setInt(4, colorItemSizeStocks.get(i).getStock());
            }
            @Override
            public int getBatchSize() {
                return colorItemSizeStocks.size();
            }
        });
    }

    public void saveColorItemImages(List<ColorItemImage> colorItemImages) {

        jdbcTemplate.batchUpdate("INSERT INTO color_item_image(color_item_image_id, color_item_id, original_file_name, stored_file_name, sequence) values (?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, colorItemImages.get(i).getId());
                ps.setLong(2, colorItemImages.get(i).getColorItem().getId());
                ps.setString(3, colorItemImages.get(i).getUploadFile().getOriginalFileName());
                ps.setString(4, colorItemImages.get(i).getUploadFile().getStoredFileName());
                ps.setInt(5, colorItemImages.get(i).getSequence());
            }
            @Override
            public int getBatchSize() {
                return colorItemImages.size();
            }
        });
    }


    //------------fegin로직-----------------
    public void decreaseStock(Long colorItemSizeStockId, int quantity){
        //동시에 요청 오면, UPDATE 전에 재고 조회하는 게 의미가 없음
        //단일 요청이면 의미 있긴한데, 밑에 update 쿼리만으로 재고 부족 예외 throw 가능
        //그래서 if(db.stock - quantity < 0) throw 로직 제거

        //변경 감지는 갱실 분실 문제 발생 -> 직접 update로 배타적 락으로 예방
        int updated = em.createQuery("update ColorItemSizeStock c set c.stock = c.stock-:quantity " +
                        "where c.id = :id and c.stock >= :quantity")
                .setParameter("quantity", quantity)
                .setParameter("id", colorItemSizeStockId)
                .executeUpdate();

        //재고보다 주문양이 많으면 업데이트 된 row 없는 걸 이용
        if (updated == 0)
            throw new NotEnoughStockException();
    }

    public void decreaseStocks(List<OrderItem> orderItems){
        for (OrderItem orderItem : orderItems) {
            decreaseStock(orderItem.getColorItemSizeStock().getId(), orderItem.getCount());
        }
    }

    /*
    public void increaseStock(Long colorItemSizeStockId, int quantity){
        //동시에 요청 오면, UPDATE 전에 재고 조회하는 게 의미가 없음
        //단일 요청이면 의미 있긴한데, 밑에 update 쿼리만으로 재고 부족 예외 throw 가능
        //그래서 if(db.stock - quantity < 0) throw 로직 제거

        //변경 감지는 갱실 분실 문제 발생 -> 직접 update로 배타적 락으로 예방
        int updated = em.createQuery("update ColorItemSizeStock c set c.stock = c.stock + :quantity " +
                        "where c.id = :id and c.stock >= :quantity")
                .setParameter("quantity", quantity)
                .setParameter("id", colorItemSizeStockId)
                .executeUpdate();

    }

    public void increaseStocks(List<OrderItemFeignResponse> responses){
        for (OrderItemFeignResponse response : responses) {
            increaseStock(response.colorItemSizeStockId(), response.count());
        }
    }
    */


}
