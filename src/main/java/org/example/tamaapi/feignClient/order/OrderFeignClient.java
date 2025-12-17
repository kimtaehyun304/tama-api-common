package org.example.tamaapi.feignClient.order;

import org.example.tamaapi.domain.order.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "order-service", url = "http://localhost:5001")
public interface OrderFeignClient {


    @GetMapping("/api/orders/{orderId}/full")
    FullOrderResponse getFullOrder(@PathVariable Long orderId);

}
