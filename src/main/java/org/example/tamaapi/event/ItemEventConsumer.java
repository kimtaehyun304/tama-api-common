package org.example.tamaapi.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.ColorItemImage;
import org.example.tamaapi.domain.item.ColorItemSizeStock;
import org.example.tamaapi.event.ItemCreatedEvent;
import org.example.tamaapi.feignClient.item.*;
import org.example.tamaapi.repository.item.ItemRepository;
import org.example.tamaapi.service.ItemService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemEventConsumer {
    private final String ITEM_SYNC_TOPIC = "item_sync_topic";

    private final ItemFeignClient itemFeignClient;
    private final ItemService itemService;
    private final ObjectMapper objectMapper;

    /*
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ITEM_TOPIC, groupId = "item_consumer_group")
    //파라미터를 ItemCreatedEvent 로 해도 IncreaseStockEvent도 listen 함
    //안되더니 또 되네.. 다시 원복함
    public void consumeItemCreatedEvent(JsonNode payload) {
        String type = payload.get("eventType").asText();
        if (!type.equals("ITEM_CREATED")) return;

        ItemCreatedEvent event = objectMapper.convertValue(payload.get("data"), ItemCreatedEvent.class);
        ItemSyncResponse res = itemFeignClient.getItem(event.itemId());
        itemService.syncItem(res);
    }
     */

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ITEM_SYNC_TOPIC, groupId = "item_consumer_group")
    public void consumeItemCreatedEvent(ItemCreatedEvent event, Acknowledgment ack) {
        ItemSyncResponse res = itemFeignClient.getItem(event.itemId());
        itemService.syncItem(res);
        ack.acknowledge();
    }

}