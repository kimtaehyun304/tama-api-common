package org.tama.tamaapi.feignClient.order;

import lombok.*;
import org.tama.tamaapi.domain.item.ColorItemSizeStock;
import org.tama.tamaapi.domain.order.Order;
import org.tama.tamaapi.domain.order.OrderItem;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
public class FullOrderItemResponse {

    private Long id;

    private Long orderId;

    private Long colorItemSizeStockId;

    //구매 후 가격이 바뀔 수 있어서 당시 가격 남겨야함 (할인을 시작하거나, 할인이 끝나거나)
    private int orderPrice;

    private int count;

    public OrderItem toEntity(){
        Order order = new Order(orderId);
        ColorItemSizeStock colorItemSizeStock = new ColorItemSizeStock(colorItemSizeStockId);
        return new OrderItem(id, order, colorItemSizeStock, orderPrice, count);
    }

}
