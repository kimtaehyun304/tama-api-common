package org.tama.tamaapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.tama.tamaapi.domain.order.Order;
import org.tama.tamaapi.domain.order.OrderItem;

import org.tama.tamaapi.feignClient.order.FullOrderItemResponse;
import org.tama.tamaapi.feignClient.order.FullOrderResponse;
import org.tama.tamaapi.feignClient.order.OrderFeignClient;
import org.tama.tamaapi.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderFeignClient orderFeignClient;
    private final ItemService itemService;
    private final OrderTxService orderTxService;
    private final OrderRepository orderRepository;

    public void saveOrder(Long orderId){
        //db에 주문 반영
        FullOrderResponse res = orderFeignClient.getFullOrder(orderId);
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