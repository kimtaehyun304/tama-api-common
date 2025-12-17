package org.example.tamaapi.feignClient.order;

import jakarta.persistence.*;
import lombok.*;
import org.example.tamaapi.domain.item.ColorItemSizeStock;
import org.example.tamaapi.domain.order.Delivery;
import org.example.tamaapi.domain.order.Order;
import org.example.tamaapi.domain.order.OrderItem;
import org.example.tamaapi.domain.order.OrderStatus;
import org.example.tamaapi.domain.user.Guest;
import org.example.tamaapi.repository.item.ColorItemSizeStockRepository;

import java.util.ArrayList;
import java.util.List;

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
        Order order = new Order(this.orderId);
        return new OrderItem(this.id, order, this.colorItemSizeStockId, this.orderPrice, this.count);
    }

}
