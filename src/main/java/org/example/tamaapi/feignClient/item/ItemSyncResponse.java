package org.example.tamaapi.feignClient.item;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class ItemSyncResponse {

    private ItemResponse item;

    private List<ColorItemResponse> colorItems;

    private List<ColorItemSizeStockResponse> colorItemSizeStocks;

    private List<ColorItemImageResponse> colorItemImages;

}
