package org.example.pipesandfilters;

import org.example.stereotype.Message;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public class HotMessageSource<M extends Message> implements MessageSource<M> {

    private static final Logger log = LoggerFactory.getLogger(HotMessageSource.class);
    private static final ErrorHandler LOGGING_ERROR_HANDLER = (subscriber, error) -> log.error("Error notifying subscriber " + subscriber, error);
    private final ConcurrentMap<Subscription, Consumer<M>> subscribers = new ConcurrentHashMap<>();
    private final Executor publishExecutor;
    private final ErrorHandler errorHandler;

    public HotMessageSource() {
        this(null, null);
    }

    public HotMessageSource(@Nullable Executor publishExecutor, @Nullable ErrorHandler errorHandler) {
        this.publishExecutor = publishExecutor == null ? Runnable::run : publishExecutor;
        this.errorHandler = errorHandler == null ? LOGGING_ERROR_HANDLER : errorHandler;
    }

    @Override
    public @NotNull Subscription subscribe(@NotNull Consumer<M> subscriber) {
        requireNonNull(subscriber);
        var subscription = new Subscription() {
            @Override
            public void unsubscribe() {
                subscribers.remove(this);
            }
        };
        subscribers.put(subscription, subscriber);
        return subscription;
    }

    public void publish(@NotNull M message) {
        requireNonNull(message);
        publishExecutor.execute(() -> Set.copyOf(subscribers.values()).forEach(subscriber -> {
            try {
                subscriber.accept(message);
            } catch (Throwable ex) {
                errorHandler.handleError(subscriber, ex);
            }
        }));
    }

    public interface ErrorHandler {
        void handleError(@NotNull Consumer<? extends Message> subscriber, @NotNull Throwable error);
    }
}
