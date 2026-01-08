package org.example.tamaapi.exception.feign.order;

public class OrderFeignCommandException extends RuntimeException{
    public OrderFeignCommandException(String message) {
        super(message);
    }
}
