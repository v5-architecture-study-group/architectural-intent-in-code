package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public final class BuyStockOrder extends StockOrder {
    private final PositiveMoney bidPrice;

    public BuyStockOrder(long orderId, Broker broker, StockShares shares, PositiveMoney bidPrice) {
        super(orderId, broker, shares);
        this.bidPrice = requireNonNull(bidPrice);
    }

    public PositiveMoney bidPrice() {
        return bidPrice;
    }
}
