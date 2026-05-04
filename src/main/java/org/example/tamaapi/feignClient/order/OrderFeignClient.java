package org.example.tamaapi.feignClient.order;


import org.example.tamaapi.exception.feign.order.OrderFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//k8s로 바꾸면서 url 옵션 생략
@FeignClient(name = "order-service"
        ,configuration = OrderFeignClientConfig.class
        ,fallbackFactory = OrderFallbackFactory.class)
public interface OrderFeignClient {

    @GetMapping("/api/orders/{orderId}/full")
    FullOrderResponse getFullOrder(@PathVariable Long orderId);

}
