package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public final class Stock {
    private final String tickerSymbol;

    public Stock(String tickerSymbol) {
        this.tickerSymbol = requireNonNull(tickerSymbol);
        // TODO Validate tickerSymbol
    }

    @Override
    public String toString() {
        return tickerSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(tickerSymbol, stock.tickerSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbol);
    }
}
