package org.example.components.marketprice;

import org.example.components.orderprocessing.domain.primitives.PositiveMoney;
import org.example.components.orderprocessing.domain.primitives.Stock;
import org.example.components.orderprocessing.messages.TransactionCreatedEvent;
import org.example.pipesandfilters.HasInput;
import org.example.pipesandfilters.MessageSink;
import org.example.stereotype.Component;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@ThreadSafe
public final class MarketPriceComponent implements HasInput<TransactionCreatedEvent> {
    private final ConcurrentMap<Stock, PositiveMoney> currentMarketPrice = new ConcurrentHashMap<>();

    private void onTransactionCreatedEvent(@NotNull TransactionCreatedEvent event) {
        currentMarketPrice.put(event.transaction().stock(), event.transaction().pricePerShare());
    }

    public @NotNull Optional<PositiveMoney> getMarketPrice(@NotNull Stock stock) {
        return Optional.ofNullable(currentMarketPrice.get(stock));
    }

    @Override
    public @NotNull MessageSink<TransactionCreatedEvent> input() {
        return this::onTransactionCreatedEvent;
    }
}
