package org.example.components.orderprocessing.domain.model.queues;

import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

@ThreadSafe
final class ExpiringConditionalQueue<E> {

    private final ConditionalQueue<Entry<E>> queue;

    ExpiringConditionalQueue(@NotNull ConditionalQueue.QueueFactory queueFactory, @Nullable Lock pushLock, @Nullable Lock pollLock) {
        queue = new ConditionalQueue<>(queueFactory, pushLock, pollLock);
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
