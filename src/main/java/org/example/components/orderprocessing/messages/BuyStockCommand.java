package org.example.components.orderprocessing.messages;

import org.example.components.orderprocessing.domain.primitives.*;
import org.example.stereotype.Command;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public record BuyStockCommand(
        @NotNull OrderId orderId,
        @NotNull Broker broker,
        @NotNull Stock stock,
        @NotNull Shares shares,
        @NotNull PositiveMoney bidPricePerShare,
        @NotNull Duration cancelAfter
) implements Command {
    public BuyStockCommand {
        requireNonNull(orderId);
        requireNonNull(broker);
        requireNonNull(stock);
        requireNonNull(shares);
        requireNonNull(bidPricePerShare);
        requireNonNull(cancelAfter);
    }
}
