package org.example.tamaapi.feignClient.order;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "order-service", url = "http://localhost:5001")
public interface OrderFeignClient {

    @GetMapping("/api/orders/{orderId}/full")
    FullOrderResponse getFullOrder(@PathVariable Long orderId);

}
