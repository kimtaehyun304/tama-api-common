package org.example.tamaapi.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.tamaapi.domain.EventType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private final EventType eventType = EventType.ORDER_CREATED;
    private Long orderId;

}
