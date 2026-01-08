package org.example.tamaapi.exception.feign.item;

public class ItemFeignQueryException extends RuntimeException{
    public ItemFeignQueryException(String message) {
        super(message);
    }
}
