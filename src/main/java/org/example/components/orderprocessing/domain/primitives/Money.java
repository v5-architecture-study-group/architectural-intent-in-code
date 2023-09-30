package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ThreadSafe
@DomainPrimitive
public sealed class Money implements Comparable<Money> permits PositiveMoney {
    private final Currency currency;
    private final BigDecimal amount;

    public Money(@NotNull Currency currency, @NotNull BigDecimal amount) {
        this.currency = requireNonNull(currency);
        this.amount = requireNonNull(amount).setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
    }

    public @NotNull Currency currency() {
        return currency;
    }

    public @NotNull BigDecimal amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(currency, money.currency) && Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }

    @Override
    public int compareTo(@NotNull Money o) {
        return amount.compareTo(o.amount);
    }
}
