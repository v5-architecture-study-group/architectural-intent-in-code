package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.*;
import org.example.stereotype.Entity;
import org.example.stereotype.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

@Entity
@NotThreadSafe
public final class Bid extends AbstractOrder {
    private final PositiveMoney bidPricePerShare;
    private Shares shares;

    public Bid(@NotNull BidId orderId, @NotNull Broker broker, @NotNull Stock stock, @NotNull Duration validFor, @NotNull PositiveMoney bidPricePerShare, @NotNull Shares shares) {
        super(orderId, broker, stock, validFor);
        this.bidPricePerShare = requireNonNull(bidPricePerShare);
        this.shares = requireNonNull(shares);
    }

    public @NotNull Shares shares() {
        return shares;
    }

    public @NotNull PositiveMoney bidPricePerShare() {
        return bidPricePerShare;
    }

    public void sharesBought(@NotNull Shares shares) {
        this.shares = this.shares.minus(shares);
    }
}
