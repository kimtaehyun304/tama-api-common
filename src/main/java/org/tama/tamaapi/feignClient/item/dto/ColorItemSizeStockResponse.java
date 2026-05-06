package org.tama.tamaapi.feignClient.item.dto;

import lombok.*;
import org.tama.tamaapi.domain.item.ColorItem;
import org.tama.tamaapi.domain.item.ColorItemSizeStock;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class ColorItemSizeStockResponse {

    private Long id;

    private Long colorItemId;

    private String size;

    private int stock;

    public ColorItemSizeStock toEntity() {
        ColorItem colorItem = new ColorItem(colorItemId);
        return new ColorItemSizeStock(id, colorItem, size);
    }

}
