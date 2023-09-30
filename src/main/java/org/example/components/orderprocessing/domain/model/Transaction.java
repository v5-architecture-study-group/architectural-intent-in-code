package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.*;
import org.example.stereotype.Entity;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ThreadSafe
@Entity
public final class Transaction {

    private final TransactionId transactionId;
    private final Stock stock;
    private final Broker bidder;
    private final OrderId bidId;
    private final Broker seller;
    private final OrderId askId;
    private final Shares shares;
    private final PositiveMoney pricePerShare;

    public Transaction(@NotNull TransactionId transactionId,
                       @NotNull Stock stock,
                       @NotNull Broker bidder,
                       @NotNull OrderId bidId,
                       @NotNull Broker seller,
                       @NotNull OrderId askId,
                       @NotNull Shares shares,
                       @NotNull PositiveMoney pricePerShare) {
        this.transactionId = requireNonNull(transactionId);
        this.stock = requireNonNull(stock);
        this.bidder = requireNonNull(bidder);
        this.bidId = requireNonNull(bidId);
        this.seller = requireNonNull(seller);
        this.askId = requireNonNull(askId);
        this.shares = requireNonNull(shares);
        this.pricePerShare = requireNonNull(pricePerShare);
    }

    public @NotNull TransactionId transactionId() {
        return transactionId;
    }

    public @NotNull Stock stock() {
        return stock;
    }

    public @NotNull Broker bidder() {
        return bidder;
    }

    public @NotNull OrderId bidId() {
        return bidId;
    }

    public @NotNull Broker seller() {
        return seller;
    }

    public @NotNull OrderId askId() {
        return askId;
    }

    public @NotNull Shares shares() {
        return shares;
    }

    public @NotNull PositiveMoney pricePerShare() {
        return pricePerShare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
