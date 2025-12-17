package org.example.tamaapi.feignClient.item;

import lombok.*;
import org.example.tamaapi.domain.item.Color;
import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.ColorItemSizeStock;

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
        return new ColorItemSizeStock(id, colorItem, size, stock);
    }

}
