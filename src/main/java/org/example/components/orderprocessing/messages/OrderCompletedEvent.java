package org.example.components.orderprocessing.messages;

import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.stereotype.Event;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public record OrderCompletedEvent(@NotNull OrderId orderId) implements Event {

    public OrderCompletedEvent {
        requireNonNull(orderId);
    }
}
