package org.example.tamaapi.feignClient.item;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "item-service", url = "http://localhost:5002")
public interface ItemFeignClient {

    @GetMapping("/api/items/{itemId}")
    ItemSyncResponse getItem(@PathVariable Long itemId);

}
