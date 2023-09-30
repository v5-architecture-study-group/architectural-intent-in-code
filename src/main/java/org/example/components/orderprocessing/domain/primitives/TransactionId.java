package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

@DomainPrimitive
@ThreadSafe
public record TransactionId(long sequenceNumber, @NotNull OrderId askId, @NotNull OrderId bidId) {
    public TransactionId {
        requireNonNull(askId);
        requireNonNull(bidId);
    }

    @Override
    public String toString() {
        return "%d-%s-%s".formatted(sequenceNumber(), askId(), bidId());
    }
}
