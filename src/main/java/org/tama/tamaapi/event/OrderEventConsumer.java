package org.tama.tamaapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tama.tamaapi.domain.order.OrderStatus;
import org.tama.tamaapi.service.OrderService;
import org.tama.tamaapi.service.OrderTxService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {
    private final String ORDER_SYNC_TOPIC = "order_sync_topic";

    private final OrderService orderService;
    private final OrderTxService orderTxService;

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

    /*
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ORDER_SYNC_TOPIC, groupId = "order_consumer_group")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event, Acknowledgment ack) {
        orderService.saveOrder(event.getOrderId());
        ack.acknowledge();
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ORDER_SYNC_TOPIC, groupId = "order_consumer_group")
    public void consumeOrderCanceledEvent(OrderCanceledEvent event, Acknowledgment ack) {
        orderService.cancelOrder(event.getOrderId());
        ack.acknowledge();
    }
    */

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ORDER_SYNC_TOPIC, groupId = "order_consumer_group")
    public void consumeOrderCreatedEvent(OrderEvent event, Acknowledgment ack) {
        switch (event.getEventType()) {
            case ORDER_RECEIVED -> orderService.saveOrder(event.getOrderId());
            default -> orderTxService.updateOrderStatus(event.getOrderId(), OrderStatus.valueOf(event.getEventType().name()));
        }
        ack.acknowledge();
        log.info("주문 테이블 동기화 완료");
    }

}