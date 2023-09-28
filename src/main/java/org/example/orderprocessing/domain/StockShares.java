package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public final class StockShares {
    private final Stock stock;
    private final int numberOfShares;

    public StockShares(Stock stock, int numberOfShares) {
        this.stock = requireNonNull(stock);
        this.numberOfShares = numberOfShares;
        if (numberOfShares <= 0) {
            throw new IllegalArgumentException("numberOfShares must be > 0");
        }
    }

    public Stock stock() {
        return stock;
    }

    public int numberOfShares() {
        return numberOfShares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockShares that = (StockShares) o;
        return numberOfShares == that.numberOfShares && Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, numberOfShares);
    }
}
