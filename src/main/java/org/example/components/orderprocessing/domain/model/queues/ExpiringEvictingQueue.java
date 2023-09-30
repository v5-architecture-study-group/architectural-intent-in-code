package org.example.components.orderprocessing.domain.model.queues;

import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

@ThreadSafe
final class ExpiringEvictingQueue<E> {

    private final EvictingQueue<Entry<E>> queue;

    ExpiringEvictingQueue(@NotNull EvictingQueue.QueueFactory queueFactory, @Nullable Lock pushLock, @Nullable Lock pollLock, @Nullable EvictingQueueOutputPort<E> outputPort) {
        queue = new EvictingQueue<>(queueFactory, pushLock, pollLock, outputPort == null ? null : e -> outputPort.elementEvicted(e.element));
    }

    public void push(@NotNull E element, @NotNull Duration keepFor) {
        queue.push(new Entry<>(element, System.currentTimeMillis() + keepFor.toMillis()));
    }

    public @Nullable E peek(@NotNull Predicate<E> condition) {
        var result = queue.peek(e -> isValid(e, condition));
        return result == null ? null : result.element;
    }

    public @Nullable E poll(@NotNull Predicate<E> condition) {
        var result = queue.poll(e -> isValid(e, condition));
        return result == null ? null : result.element;
    }

    boolean isValid(@NotNull Entry<E> entry, @NotNull Predicate<E> condition) {
        return condition.test(entry.element) && System.currentTimeMillis() <= entry.notValidAfter;
    }

    record Entry<E>(@NotNull E element, long notValidAfter) {
    }
}
