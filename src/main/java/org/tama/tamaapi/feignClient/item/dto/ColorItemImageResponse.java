package org.tama.tamaapi.feignClient.item.dto;

import lombok.*;
import org.tama.sharelib.common.util.UploadFile;
import org.tama.tamaapi.domain.item.ColorItem;
import org.tama.tamaapi.domain.item.ColorItemImage;

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
