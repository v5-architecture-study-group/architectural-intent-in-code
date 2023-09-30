package org.example.components.orderprocessing.messages;

import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.stereotype.Command;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public record CancelOrderCommand(@NotNull OrderId orderId) implements Command {

    public CancelOrderCommand {
        requireNonNull(orderId);
    }
}
