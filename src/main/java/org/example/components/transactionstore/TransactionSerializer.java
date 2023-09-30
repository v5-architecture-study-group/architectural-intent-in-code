package org.example.components.transactionstore;

import org.example.components.orderprocessing.domain.model.Transaction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

public interface TransactionSerializer {
    void serialize(@NotNull Transaction transaction, @NotNull OutputStream out) throws IOException;
}
