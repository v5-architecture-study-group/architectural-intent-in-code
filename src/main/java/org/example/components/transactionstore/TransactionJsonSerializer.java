package org.example.components.transactionstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.components.orderprocessing.domain.model.Transaction;
import org.example.stereotype.ComponentDelegate;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

@ComponentDelegate
@ThreadSafe
public final class TransactionJsonSerializer implements TransactionSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void serialize(@NotNull Transaction transaction, @NotNull OutputStream out) throws IOException {
        objectMapper.writerFor(Transaction.class).writeValue(out, transaction);
    }
}
