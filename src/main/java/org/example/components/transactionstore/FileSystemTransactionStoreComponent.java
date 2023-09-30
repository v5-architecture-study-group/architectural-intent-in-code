package org.example.components.transactionstore;

import org.example.components.orderprocessing.domain.model.Transaction;
import org.example.components.orderprocessing.domain.primitives.TransactionId;
import org.example.components.orderprocessing.messages.TransactionCreatedEvent;
import org.example.pipesandfilters.HasInput;
import org.example.pipesandfilters.MessageSink;
import org.example.stereotype.Component;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

@Component
@ThreadSafe
public final class FileSystemTransactionStoreComponent implements HasInput<TransactionCreatedEvent> {

    private final TransactionSerializer transactionSerializer;
    private final File directory;
    private final Executor storeExecutor;
    private final TransactionStoreErrorHandler errorHandler;

    public FileSystemTransactionStoreComponent(@NotNull TransactionSerializer transactionSerializer,
                                               @NotNull File directory) throws IOException {
        this(transactionSerializer, directory, null, null);
    }

    public FileSystemTransactionStoreComponent(@NotNull TransactionSerializer transactionSerializer,
                                               @NotNull File directory,
                                               @Nullable Executor storeExecutor,
                                               @Nullable TransactionStoreErrorHandler errorHandler) throws IOException {
        this.transactionSerializer = requireNonNull(transactionSerializer);
        this.directory = requireNonNull(directory);
        this.storeExecutor = storeExecutor == null ? Runnable::run : storeExecutor;
        this.errorHandler = errorHandler == null ? LoggingTransactionStoreErrorHandler.singleton() : errorHandler;

        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                throw new IOException("Directory could not be created");
            }
        }
    }

    private void storeTransaction(@NotNull Transaction transaction) {
        storeExecutor.execute(() -> {
            try {
                var f = new File(directory, toFileName(transaction.transactionId()));
                try (var fc = FileChannel.open(f.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW)) {
                    transactionSerializer.serialize(transaction, Channels.newOutputStream(fc));
                }
            } catch (Throwable error) {
                errorHandler.handleError(transaction, error);
            }
        });
    }

    private @NotNull String toFileName(@NotNull TransactionId transactionId) {
        return "%d-%s".formatted(System.currentTimeMillis(), transactionId.toString());
    }

    @Override
    public @NotNull MessageSink<TransactionCreatedEvent> input() {
        return event -> storeTransaction(event.transaction());
    }
}
