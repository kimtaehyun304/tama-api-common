package org.example.tamaapi.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tamaapi.domain.order.Order;
import org.example.tamaapi.domain.order.OrderItem;
import org.example.tamaapi.feignClient.order.FullOrderItemResponse;
import org.example.tamaapi.feignClient.order.FullOrderResponse;
import org.example.tamaapi.feignClient.order.OrderFeignClient;
import org.example.tamaapi.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderTxService {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;
    private final OrderRepository orderRepository;


    @Transactional
    public void saveOrder(Order order, List<OrderItem> orderItems){
        //data jpa save쓰면 pk 있어서 merge 발생해서 저장 안됨 (될때도 있던데 왠지는 모름)
        em.persist(order);
        em.flush();
        saveOrderItems(orderItems);
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