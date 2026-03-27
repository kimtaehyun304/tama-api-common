package org.example.tamaapi.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tamaapi.domain.order.OrderItem;
import org.example.tamaapi.feignClient.item.ItemSyncResponse;
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
    private final String ORDER_SYNC_TOPIC = "order_sync_topic";

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    /*
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ORDER_TOPIC, groupId = "order_consumer_group")
    public void consumeOrderCreatedEvent(JsonNode payload) {
        String type = payload.get("eventType").asText();
        if (!type.equals("ORDER_CREATED")) return;
        OrderCreatedEvent event = objectMapper.convertValue(payload.get("data"), OrderCreatedEvent.class);
        orderService.syncOrder(event.getOrderId());
    }
    */
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ORDER_SYNC_TOPIC, groupId = "order_consumer_group")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event, Acknowledgment ack) {
        orderService.syncOrder(event.getOrderId());
        ack.acknowledge();
    }
}