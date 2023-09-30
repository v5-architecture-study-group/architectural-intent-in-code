package org.example.components.transactionstore;

import org.example.components.orderprocessing.domain.model.Transaction;
import org.jetbrains.annotations.NotNull;

public interface TransactionStoreErrorHandler {
    void handleError(@NotNull Transaction transaction, @NotNull Throwable error);
}
