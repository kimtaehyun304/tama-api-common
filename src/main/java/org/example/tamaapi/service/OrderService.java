package org.example.tamaapi.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.tamaapi.domain.order.Order;
import org.example.tamaapi.domain.order.OrderItem;

import org.example.tamaapi.feignClient.order.FullOrderItemResponse;
import org.example.tamaapi.feignClient.order.FullOrderResponse;
import org.example.tamaapi.feignClient.order.OrderFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;
    private final OrderFeignClient orderFeignClient;
    private final ItemService itemService;


    @Value("${portOne.secret}")
    private String PORT_ONE_SECRET;

    public static Double POINT_ACCUMULATION_RATE = 0.005;

    public void syncOrder(Long orderId){
        //db에 주문 반영
        FullOrderResponse res = orderFeignClient.getFullOrder(orderId);
        save(res.ToEntity());

        List<OrderItem> orderItems = res.getOrderItems().stream().map(FullOrderItemResponse::toEntity).toList();

        saveOrderItems(orderItems);

        //상품 반영
        itemService.decreaseStocks(orderItems);
        // 회원 msa 호출은 안해도 됨 (데이터 안 갖고 있기 떄문)
    }

    // syncItem에서 직접 호출해서 트랜잭션 발동 하지 않음 -> 쓰기 지연
    // flush로 insert 쿼리, 바로 실행하게 함
    public void save(Order order){
        em.persist(order);
        em.flush();
    }


    public void saveOrderItems(List<OrderItem> orderItems) {
        jdbcTemplate.batchUpdate("INSERT INTO order_item(order_id, color_item_size_stock_id, order_price, count) values (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, orderItems.get(i).getOrder().getId());
                ps.setLong(2, orderItems.get(i).getColorItemSizeStock().getId());
                ps.setInt(3, orderItems.get(i).getOrderPrice());
                ps.setInt(4, orderItems.get(i).getCount());
            }
            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });
    }



}