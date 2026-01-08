package org.example.tamaapi.feignClient.item;

import static org.example.tamaapi.exception.CommonExceptionHandler.throwOriginalException;


public class ItemFallback implements ItemFeignClient{

    private final Throwable cause;

    public ItemFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public ItemSyncResponse getItem(Long itemId) {
        throwOriginalException(cause);
        return null;
    }
}
