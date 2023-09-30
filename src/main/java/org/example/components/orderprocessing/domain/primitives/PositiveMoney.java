package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Currency;

@ThreadSafe
@DomainPrimitive
public final class PositiveMoney extends Money {
    public PositiveMoney(@NotNull Currency currency, @NotNull BigDecimal amount) {
        super(currency, amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive (> 0)");
        }
    }
}
