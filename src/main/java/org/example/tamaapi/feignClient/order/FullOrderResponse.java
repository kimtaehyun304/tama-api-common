package org.example.tamaapi.feignClient.order;

import lombok.*;
import org.example.tamaapi.domain.BaseEntity;
import org.example.tamaapi.domain.order.Delivery;
import org.example.tamaapi.domain.order.Order;
import org.example.tamaapi.domain.order.OrderItem;
import org.example.tamaapi.domain.order.OrderStatus;
import org.example.tamaapi.domain.user.Guest;
import org.example.tamaapi.domain.user.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
@ToString
public class FullOrderResponse {

    private Long id;

    private Long memberId;

    private Delivery delivery;

    private OrderStatus status;

    private Guest guest;

    private Long memberCouponId;

    private int usedCouponPrice;

    private int usedPoint;

    private int shippingFee;

    private String paymentId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<FullOrderItemResponse> orderItems = new ArrayList<>();

    public Order ToEntity(){
        Member member = new Member(this.memberId);
        return new Order(this.id, member, this.delivery, this.status, this.guest,
                this.memberCouponId, this.usedCouponPrice, this.usedPoint, this.shippingFee, this.paymentId,
                this.createdAt, this.updatedAt);
    }

}
