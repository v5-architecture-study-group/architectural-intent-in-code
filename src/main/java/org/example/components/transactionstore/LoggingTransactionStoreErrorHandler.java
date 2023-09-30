package org.example.components.transactionstore;

import org.example.components.orderprocessing.domain.model.Transaction;
import org.example.stereotype.ComponentDelegate;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentDelegate
@ThreadSafe
public final class LoggingTransactionStoreErrorHandler implements TransactionStoreErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(LoggingTransactionStoreErrorHandler.class);
    private static final TransactionStoreErrorHandler INSTANCE = new LoggingTransactionStoreErrorHandler();

    public static @NotNull TransactionStoreErrorHandler singleton() {
        return INSTANCE;
    }

    @Override
    public void handleError(@NotNull Transaction transaction, @NotNull Throwable error) {
        log.error("Error storing transaction " + transaction.transactionId(), error);
    }
}
