package org.example.tamaapi.event;

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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemEventConsumer {
    private final String ITEM_TOPIC = "item_topic";

    private final ItemFeignClient itemFeignClient;
    private final ItemService itemService;

    @KafkaListener(topics = ITEM_TOPIC)
    public void consumeItemCreatedEvent(ItemCreatedEvent event) {
        ItemSyncResponse res = itemFeignClient.getItem(event.itemId());
        itemService.syncItem(res);
    }
}