package org.tama.tamaapi.feignClient.order;

import lombok.*;
import org.tama.tamaapi.domain.order.Delivery;
import org.tama.tamaapi.domain.order.Order;
import org.tama.tamaapi.domain.order.OrderStatus;
import org.tama.tamaapi.domain.user.Guest;
import org.tama.tamaapi.domain.user.Member;

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
        Member member = new Member(memberId);
        return new Order(id, member, delivery, status, guest,
                memberCouponId, usedCouponPrice, usedPoint, shippingFee, paymentId,
                createdAt, updatedAt);
    }

}
