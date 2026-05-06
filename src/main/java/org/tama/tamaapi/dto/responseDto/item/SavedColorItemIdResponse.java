package org.tama.tamaapi.dto.responseDto.item;

import lombok.Getter;

import java.util.List;


@Getter
public class SavedColorItemIdResponse {

    private final List<Long> savedColorItemIds;

    public SavedColorItemIdResponse(List<Long> savedColorItemIds) {
        this.savedColorItemIds = savedColorItemIds;
    }
}
