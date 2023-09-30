package org.example.components.orderprocessing.messages;

import org.example.components.orderprocessing.domain.primitives.*;
import org.example.stereotype.Command;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public record SellStockCommand(
        @NotNull AskId orderId,
        @NotNull Broker broker,
        @NotNull Stock stock,
        @NotNull Shares shares,
        @NotNull PositiveMoney askPricePerShare,
        @NotNull Duration validFor
) implements Command {
    public SellStockCommand {
        requireNonNull(orderId);
        requireNonNull(broker);
        requireNonNull(stock);
        requireNonNull(shares);
        requireNonNull(askPricePerShare);
        requireNonNull(validFor);
    }
}
