package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface PrioritizedAskQueue {
    @NotNull Optional<Ask> getBestAsk(@NotNull Bid bid);

    void push(@NotNull Ask ask);

    void cancel(@NotNull OrderId orderId);

    void complete(@NotNull Ask ask);
}
