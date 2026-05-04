package org.example.tamaapi.feignClient.item;


import org.example.tamaapi.exception.feign.item.ItemFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//k8s로 바꾸면서 url 옵션 생략
@FeignClient(name = "item-service"
        , configuration = ItemFeignClientConfig.class
        , fallbackFactory = ItemFallbackFactory.class)
public interface ItemFeignClient {

    @GetMapping("/api/items/{itemId}")
    ItemSyncResponse getItem(@PathVariable Long itemId);

}
