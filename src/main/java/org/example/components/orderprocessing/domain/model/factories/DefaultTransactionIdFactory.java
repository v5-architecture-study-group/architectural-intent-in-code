package org.example.components.orderprocessing.domain.model.factories;

import org.example.components.orderprocessing.domain.model.TransactionIdFactory;
import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.components.orderprocessing.domain.primitives.TransactionId;
import org.example.stereotype.ComponentDelegate;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

@ComponentDelegate
@ThreadSafe
public class DefaultTransactionIdFactory implements TransactionIdFactory {
    private final AtomicLong nextSequence;

    public DefaultTransactionIdFactory(long nextSequence) {
        this.nextSequence = new AtomicLong(nextSequence);
    }

    public DefaultTransactionIdFactory() {
        this(0L);
    }

    @Override
    public @NotNull TransactionId createNewTransactionId(@NotNull OrderId askId, @NotNull OrderId bidId) {
        return new TransactionId(nextSequence.getAndIncrement(), askId, bidId);
    }
}
