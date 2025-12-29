package org.example.tamaapi.domain.order;

import jakarta.persistence.*;
import lombok.*;
import org.example.tamaapi.domain.item.ColorItemSizeStock;


@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  OrderItem {

    @Id
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_item_size_stock_id")
    private ColorItemSizeStock colorItemSizeStock;

    //구매 후 가격이 바뀔 수 있어서 당시 가격 남겨야함 (할인을 시작하거나, 할인이 끝나거나)
    private int orderPrice;

    private int count;

    public OrderItem(Long id, Order order, ColorItemSizeStock colorItemSizeStock, int orderPrice, int count) {
        this.id = id;
        this.order = order;
        this.colorItemSizeStock = colorItemSizeStock;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
