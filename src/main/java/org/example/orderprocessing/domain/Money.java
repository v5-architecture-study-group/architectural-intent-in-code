package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public sealed class Money permits PositiveMoney {
    private final Currency currency;
    private final BigDecimal amount;

    public Money(Currency currency, BigDecimal amount) {
        this.currency = requireNonNull(currency);
        this.amount = requireNonNull(amount).setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
    }

    public Currency currency() {
        return currency;
    }

    public BigDecimal amount() {
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
}
