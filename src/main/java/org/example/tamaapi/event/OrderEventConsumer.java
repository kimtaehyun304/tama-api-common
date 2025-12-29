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
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {
    private final String ORDER_TOPIC = "order_topic";

    private final OrderService orderService;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ORDER_TOPIC, groupId = "order_consumer_group")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event, Acknowledgment ack) {
        orderService.syncOrder(event.orderId());
        ack.acknowledge();
    }
}