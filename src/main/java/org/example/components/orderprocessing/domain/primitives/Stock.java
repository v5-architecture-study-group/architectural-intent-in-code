package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ThreadSafe
@DomainPrimitive
public final class Stock {
    private final String tickerSymbol;

    public Stock(@NotNull String tickerSymbol) {
        this.tickerSymbol = requireNonNull(tickerSymbol);
        if (tickerSymbol.length() < 2) {
            throw new IllegalArgumentException("Ticket symbol is to short");
        }
        if (!tickerSymbol.codePoints().allMatch(Character::isAlphabetic)) {
            throw new IllegalArgumentException("Ticket symbol must be alphabetic");
        }
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
