package org.example.tamaapi.feignClient.order;


import org.example.tamaapi.exception.feign.member.MemberFeignClientConfig;
import org.example.tamaapi.exception.feign.order.OrderFeignClientConfig;
import org.example.tamaapi.feignClient.member.MemberFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "order-service", url = "http://localhost:5001"
        ,configuration = OrderFeignClientConfig.class
        ,fallbackFactory = OrderFallbackFactory.class)
public interface OrderFeignClient {

    @GetMapping("/api/orders/{orderId}/full")
    FullOrderResponse getFullOrder(@PathVariable Long orderId);

}
