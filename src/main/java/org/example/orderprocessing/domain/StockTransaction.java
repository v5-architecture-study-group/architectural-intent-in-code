package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import java.util.Objects;

@ThreadSafe
public class StockTransaction {

    private final long transactionId;
    private final Broker bidder;
    private final Broker seller;
    private final StockShares shares;
    private final PositiveMoney pricePerShare;

    public StockTransaction(long transactionId, Broker bidder, Broker seller, StockShares shares, PositiveMoney pricePerShare) {
        this.transactionId = transactionId;
        this.bidder = Objects.requireNonNull(bidder);
        this.seller = Objects.requireNonNull(seller);
        this.shares = Objects.requireNonNull(shares);
        this.pricePerShare = Objects.requireNonNull(pricePerShare);
    }

    public long transactionId() {
        return transactionId;
    }

    public Broker bidder() {
        return bidder;
    }

    public Broker seller() {
        return seller;
    }

    public StockShares shares() {
        return shares;
    }

    public PositiveMoney pricePerShare() {
        return pricePerShare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockTransaction that = (StockTransaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
