package org.example.tamaapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tamaapi.domain.order.OrderItem;
import org.example.tamaapi.feignClient.order.FullOrderItemResponse;
import org.example.tamaapi.feignClient.order.FullOrderResponse;
import org.example.tamaapi.feignClient.order.OrderFeignClient;
import org.example.tamaapi.service.ItemService;
import org.example.tamaapi.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {
    private final String ORDER_TOPIC = "order_topic";

    private final OrderService orderService;


    @KafkaListener(topics = ORDER_TOPIC)
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        orderService.syncOrder(event.orderId());
    }
}