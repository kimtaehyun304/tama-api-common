package org.tama.tamaapi.dto.responseDto.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tama.sharelib.common.util.UploadFile;
import org.tama.tamaapi.domain.item.ColorItemImage;

@Getter
// 대표 이미지 이외 저장
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImageDto {

    private Long id;

    private Long colorItemId;

    private UploadFile uploadFile;

    private Integer sequence;

    public ItemImageDto(ColorItemImage colorItemImage) {
        id = colorItemImage.getId();
        colorItemId = colorItemImage.getColorItem().getId();
        uploadFile = colorItemImage.getUploadFile();
        sequence = colorItemImage.getSequence();
    }
}
