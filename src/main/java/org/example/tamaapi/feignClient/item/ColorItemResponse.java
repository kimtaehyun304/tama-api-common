package org.example.tamaapi.feignClient.item;

import lombok.*;
import org.example.tamaapi.domain.item.Color;
import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.Item;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class ColorItemResponse {

    private Long id;

    private Long itemId;

    private Long colorId;

    public ColorItem toEntity() {
        Item item = new Item(itemId);
        Color color = new Color(colorId);
        return new ColorItem(id, item, color);
    }


}
