package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.components.orderprocessing.domain.primitives.TransactionId;
import org.example.stereotype.Factory;
import org.jetbrains.annotations.NotNull;

@Factory
public interface TransactionIdFactory {
    @NotNull TransactionId createNewTransactionId(@NotNull OrderId askId, @NotNull OrderId bidId);
}
