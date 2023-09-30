package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.*;
import org.example.stereotype.Entity;
import org.example.stereotype.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

@Entity
@NotThreadSafe
public final class Ask extends AbstractOrder {
    private final PositiveMoney askPricePerShare;
    private Shares shares;

    public Ask(@NotNull OrderId orderId, @NotNull Broker broker, @NotNull Stock stock, @NotNull Duration validFor,
               @NotNull PositiveMoney askPricePerShare, @NotNull Shares shares) {
        super(orderId, broker, stock, validFor);
        this.askPricePerShare = requireNonNull(askPricePerShare);
        this.shares = requireNonNull(shares);
    }

    public @NotNull PositiveMoney askPricePerShare() {
        return askPricePerShare;
    }

    public @NotNull Shares shares() {
        return shares;
    }

    public void sharesSold(@NotNull Shares shares) {
        this.shares = this.shares.minus(shares);
    }
}
