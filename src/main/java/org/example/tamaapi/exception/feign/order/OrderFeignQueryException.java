package org.example.tamaapi.exception.feign.order;

public class OrderFeignQueryException extends RuntimeException{
    public OrderFeignQueryException(String message) {
        super(message);
    }
}
