package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface PrioritizedBidQueue {
    @NotNull Optional<Bid> getNextBid();

    void push(@NotNull Bid bid);

    void cancel(@NotNull OrderId orderId);

    void complete(@NotNull Bid bid);
}
