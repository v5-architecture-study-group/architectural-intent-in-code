package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.BidId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PrioritizedBidQueue {
    @NotNull BidQueue getNextBids();

    void push(@NotNull Bid bid);

    void cancel(@NotNull BidId orderId);

    interface BidQueue {
        @Nullable Bid peek();

        @Nullable Bid poll();
    }
}
