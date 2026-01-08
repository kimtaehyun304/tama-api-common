package org.example.tamaapi.feignClient.order;


import static org.example.tamaapi.exception.CommonExceptionHandler.throwOriginalException;

public class OrderFallback implements OrderFeignClient {

    private final Throwable cause;

    public OrderFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public FullOrderResponse getFullOrder(Long orderId) {
        throwOriginalException(cause);
        return null;
    }
}
