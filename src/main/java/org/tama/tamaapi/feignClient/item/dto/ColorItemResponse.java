package org.tama.tamaapi.feignClient.item.dto;

import lombok.*;
import org.tama.tamaapi.domain.item.Color;
import org.tama.tamaapi.domain.item.ColorItem;
import org.tama.tamaapi.domain.item.Item;

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
