package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.AskId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PrioritizedAskQueue {
    @NotNull AskQueue getBestAsks(@NotNull Bid bid);

    void push(@NotNull Ask ask);

    void cancel(@NotNull AskId orderId);

    interface AskQueue {
        @Nullable Ask peek();

        @Nullable Ask poll();
    }
}
