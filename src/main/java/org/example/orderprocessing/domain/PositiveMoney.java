package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import java.math.BigDecimal;
import java.util.Currency;

@ThreadSafe
public final class PositiveMoney extends Money {
    public PositiveMoney(Currency currency, BigDecimal amount) {
        super(currency, amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive (> 0)");
        }
    }
}
