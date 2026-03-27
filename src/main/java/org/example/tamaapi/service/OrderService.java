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
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderFeignClient orderFeignClient;
    private final ItemService itemService;
    private final OrderTxService orderTxService;

    public void syncOrder(Long orderId){
        //db에 주문 반영
        FullOrderResponse res = orderFeignClient.getFullOrder(orderId);
        System.out.println("res = " + res);
        Order order = res.ToEntity();
        List<OrderItem> orderItems = res.getOrderItems().stream().map(FullOrderItemResponse::toEntity).toList();
        orderTxService.saveOrder(order, orderItems);
        // 회원 msa 호출은 안해도 됨 (데이터 안 갖고 있기 떄문)
    }

    /*
    // syncItem에서 직접 호출해서 트랜잭션 발동 하지 않음 -> 쓰기 지연
    // flush로 insert 쿼리, 바로 실행하게 함
    // 이렇게하면 트랜잭션 길어서 클래스 분리
    @Transactional
    public void save(Order order){
        em.persist(order);
        em.flush();
    }
    */

}