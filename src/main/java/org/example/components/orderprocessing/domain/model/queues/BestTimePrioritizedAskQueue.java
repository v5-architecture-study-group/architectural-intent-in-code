package org.example.components.orderprocessing.domain.model.queues;

import org.example.components.orderprocessing.domain.model.Ask;
import org.example.components.orderprocessing.domain.model.Bid;
import org.example.components.orderprocessing.domain.model.PrioritizedAskQueue;
import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.components.orderprocessing.domain.primitives.PositiveMoney;
import org.example.components.orderprocessing.domain.primitives.Stock;
import org.example.stereotype.ComponentDelegate;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

@ComponentDelegate
@ThreadSafe
public class BestTimePrioritizedAskQueue implements PrioritizedAskQueue {

    private final Set<OrderId> cancelledOrders = ConcurrentHashMap.newKeySet();
    private final Map<Stock, SortedMap<PositiveMoney, AskQueueImpl>> queues = new ConcurrentHashMap<>();
    private static final AskQueue EMPTY_QUEUE = new AskQueue() {
        @Override
        public @Nullable Ask peek() {
            return null;
        }

        @Override
        public @Nullable Ask poll() {
            return null;
        }
    };
    private final int queueCapacity;

    public BestTimePrioritizedAskQueue(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    @Override
    public @NotNull AskQueue getBestAsks(@NotNull Bid bid) {
        var asks = queues.get(bid.stock());
        if (asks == null) {
            return EMPTY_QUEUE;
        }
        var lowestAsk = asks.firstEntry();
        if (lowestAsk == null || lowestAsk.getKey().compareTo(bid.bidPricePerShare()) > 0) {
            return EMPTY_QUEUE;
        }
        return lowestAsk.getValue();
    }

    @Override
    public void push(@NotNull Ask ask) {
        queues
                .computeIfAbsent(ask.stock(), k -> new ConcurrentSkipListMap<>())
                .computeIfAbsent(ask.askPricePerShare(), k -> new AskQueueImpl())
                .push(ask);
    }

    @Override
    public void cancel(@NotNull OrderId orderId) {
        cancelledOrders.add(orderId);
    }

    private class AskQueueImpl implements AskQueue {

        private final ExpiringConditionalQueue<Ask> queue = new ExpiringConditionalQueue<>(
                new ConditionalQueue.ArrayBlockingQueueFactory(queueCapacity),
                NoLock.INSTANCE, // ArrayBlockingQueue handles the locking for us when pushing
                new ReentrantLock()
        );

        @Override
        public @Nullable Ask peek() {
            return queue.peek(ask -> !cancelledOrders.remove(ask.orderId()));
        }

        @Override
        public @Nullable Ask poll() {
            return queue.poll(ask -> !cancelledOrders.remove(ask.orderId()));
        }

        public void push(@NotNull Ask ask) {
            queue.push(ask, ask.validFor());
        }
    }
}
