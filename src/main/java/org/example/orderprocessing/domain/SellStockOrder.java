package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public final class SellStockOrder extends StockOrder {
    private final PositiveMoney askPrice;

    public SellStockOrder(long orderId, Broker broker, StockShares shares, PositiveMoney askPrice) {
        super(orderId, broker, shares);
        this.askPrice = requireNonNull(askPrice);
    }

    public PositiveMoney askPrice() {
        return askPrice;
    }
}
