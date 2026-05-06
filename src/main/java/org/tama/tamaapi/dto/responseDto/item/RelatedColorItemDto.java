package org.tama.tamaapi.dto.responseDto.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.tama.sharelib.common.util.UploadFile;
import org.tama.tamaapi.domain.item.ColorItem;

@Getter
@ToString
@Setter
public class RelatedColorItemDto {

    //ColorItemId
    private Long id;

    private String color;

    private UploadFile uploadFile;

    public RelatedColorItemDto(ColorItem colorItem, UploadFile uploadFile) {
        id = colorItem.getId();
        color = colorItem.getColor().getName();
        this.uploadFile = uploadFile;
    }

}
