package org.example.components.orderprocessing.messages;

import org.example.components.orderprocessing.domain.model.Transaction;
import org.example.stereotype.Event;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public record TransactionCreatedEvent(@NotNull Transaction transaction) implements Event {

    public TransactionCreatedEvent {
        requireNonNull(transaction);
    }
}
