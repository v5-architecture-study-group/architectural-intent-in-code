package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import java.util.Objects;

@ThreadSafe
public sealed abstract class StockOrder permits BuyStockOrder, SellStockOrder {
    private final long orderId;
    private final Broker broker;
    private final StockShares shares;

    public StockOrder(long orderId, Broker broker, StockShares shares) {
        this.orderId = orderId;
        this.broker = broker;
        this.shares = shares;
    }

    public Broker broker() {
        return broker;
    }

    public StockShares shares() {
        return shares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockOrder that = (StockOrder) o;
        return orderId == that.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
