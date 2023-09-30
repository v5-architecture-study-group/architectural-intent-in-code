package org.example.components.orderprocessing.domain.model.queues;

import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

@ThreadSafe
final class ConditionalQueue<E> {

    private final Queue<E> queue;
    private final Lock pushLock;
    private final Lock pollLock;

    ConditionalQueue(@NotNull QueueFactory queueFactory, @Nullable Lock pushLock, @Nullable Lock pollLock) {
        this.queue = queueFactory.createQueue();
        if (pushLock == null && pollLock == null) {
            this.pushLock = new ReentrantLock();
            this.pollLock = this.pushLock;
        } else if (pushLock != null && pollLock == null) {
            this.pushLock = pushLock;
            this.pollLock = pushLock;
        } else if (pushLock == null) {
            this.pushLock = pollLock;
            this.pollLock = pollLock;
        } else {
            this.pushLock = pushLock;
            this.pollLock = pollLock;
        }
    }

    public void push(@NotNull E element) {
        pushLock.lock();
        try {
            queue.add(element);
        } finally {
            pushLock.unlock();
        }
    }

    public @Nullable E peek(@NotNull Predicate<E> condition) {
        pollLock.lock();
        E element;
        try {
            while ((element = queue.peek()) != null) {
                if (condition.test(element)) {
                    return element;
                }
                queue.poll();
            }
        } finally {
            pollLock.unlock();
        }
        return null;
    }

    public @Nullable E poll(@NotNull Predicate<E> condition) {
        pollLock.lock();
        E element;
        try {
            while ((element = queue.poll()) != null) {
                if (condition.test(element)) {
                    return element;
                }
            }
        } finally {
            pollLock.unlock();
        }
        return null;
    }

    @FunctionalInterface
    public interface QueueFactory {
        <E> @NotNull Queue<E> createQueue();
    }

    public static class ArrayBlockingQueueFactory implements QueueFactory {
        private final int capacity;

        public ArrayBlockingQueueFactory(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public @NotNull <E> Queue<E> createQueue() {
            return new ArrayBlockingQueue<>(capacity);
        }
    }
}
