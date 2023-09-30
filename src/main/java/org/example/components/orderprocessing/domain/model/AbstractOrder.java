package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.Broker;
import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.components.orderprocessing.domain.primitives.Stock;
import org.example.stereotype.Entity;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

@Entity
@ThreadSafe
public abstract sealed class AbstractOrder permits Ask, Bid {
    private final OrderId orderId;
    private final Broker broker;
    private final Stock stock;
    private final Duration validFor;

    public AbstractOrder(@NotNull OrderId orderId, @NotNull Broker broker, @NotNull Stock stock, @NotNull Duration validFor) {
        this.orderId = requireNonNull(orderId);
        this.broker = requireNonNull(broker);
        this.stock = requireNonNull(stock);
        this.validFor = requireNonNull(validFor);
    }

    public @NotNull OrderId orderId() {
        return orderId;
    }

    public @NotNull Broker broker() {
        return broker;
    }

    public @NotNull Stock stock() {
        return stock;
    }

    public @NotNull Duration validFor() {
        return validFor;
    }
}
