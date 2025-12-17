package org.example.tamaapi.feignClient.item;

import lombok.*;
import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.ColorItemImage;
import org.example.tamaapi.domain.item.UploadFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class ColorItemImageResponse {

    private Long id;

    private Long colorItemId;

    private UploadFile uploadFile;

    private Integer sequence;

    public ColorItemImage toEntity() {
        ColorItem colorItem = new ColorItem(colorItemId);
        return new ColorItemImage(id, colorItem, uploadFile, sequence);
    }

}
