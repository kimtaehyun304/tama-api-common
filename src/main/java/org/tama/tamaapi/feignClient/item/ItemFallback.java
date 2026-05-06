package org.tama.tamaapi.feignClient.item;

import org.tama.tamaapi.feignClient.item.dto.ItemSyncResponse;

import static org.tama.tamaapi.exception.CommonExceptionHandler.throwOriginalException;


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
