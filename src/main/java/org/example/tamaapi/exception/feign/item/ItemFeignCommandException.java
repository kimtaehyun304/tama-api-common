package org.example.tamaapi.exception.feign.item;

public class ItemFeignCommandException extends RuntimeException{
    public ItemFeignCommandException(String message) {
        super(message);
    }
}
