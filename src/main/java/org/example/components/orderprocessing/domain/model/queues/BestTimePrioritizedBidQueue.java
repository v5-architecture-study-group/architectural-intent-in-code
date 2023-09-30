package org.example.components.orderprocessing.domain.model.queues;

import org.example.components.orderprocessing.domain.model.Bid;
import org.example.components.orderprocessing.domain.model.PrioritizedBidQueue;
import org.example.components.orderprocessing.domain.primitives.BidId;
import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.components.orderprocessing.domain.primitives.PositiveMoney;
import org.example.components.orderprocessing.domain.primitives.Stock;
import org.example.stereotype.ComponentDelegate;
import org.example.stereotype.Port;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;

@ComponentDelegate
@ThreadSafe
public final class BestTimePrioritizedBidQueue implements PrioritizedBidQueue {

    private final Set<OrderId> cancelledOrders = ConcurrentHashMap.newKeySet();
    private final Map<Stock, SortedMap<PositiveMoney, BidQueueImpl>> queues = new ConcurrentHashMap<>();
    private static final BidQueue EMPTY_QUEUE = new BidQueue() {
        @Override
        public @Nullable Bid peek() {
            return null;
        }

        @Override
        public @Nullable Bid poll() {
            return null;
        }
    };
    private final int queueCapacity;
    private final OutputPort outputPort;
    private final StockIterator stockIterator = new StockIterator();

    public BestTimePrioritizedBidQueue(int queueCapacity, @NotNull OutputPort outputPort) {
        this.queueCapacity = queueCapacity;
        this.outputPort = requireNonNull(outputPort);
    }

    @Override
    public @NotNull BidQueue getNextBids() {
        return stockIterator.getNextHighestBidQueue();
    }

    @Override
    public void push(@NotNull Bid bid) {
        queues
                .computeIfAbsent(bid.stock(), k -> new ConcurrentSkipListMap<>())
                .computeIfAbsent(bid.bidPricePerShare(), k -> new BidQueueImpl())
                .push(bid);
    }

    @Override
    public void cancel(@NotNull BidId orderId) {
        cancelledOrders.add(orderId);
    }

    private class StockIterator {

        private Iterator<Stock> stockIterator;

        public @NotNull BidQueue getNextHighestBidQueue() {
            if (queues.isEmpty()) {
                return EMPTY_QUEUE;
            }
            Stock nextStock;
            synchronized (this) {
                if (stockIterator == null || !stockIterator.hasNext()) {
                    stockIterator = queues.keySet().iterator();
                }
                nextStock = stockIterator.next();
            }
            var highestBidQueue = queues.get(nextStock).lastEntry();
            if (highestBidQueue == null) {
                return EMPTY_QUEUE;
            }
            return highestBidQueue.getValue();
        }
    }

    private class BidQueueImpl implements BidQueue {

        private final ExpiringEvictingQueue<Bid> queue = new ExpiringEvictingQueue<>(
                new EvictingQueue.ArrayBlockingQueueFactory(queueCapacity),
                NoLock.INSTANCE, // ArrayBlockingQueue handles the locking for us when pushing
                new ReentrantLock(),
                outputPort::orderCancelled
        );

        @Override
        public @Nullable Bid peek() {
            return queue.peek(bid -> !cancelledOrders.remove(bid.orderId()));
        }

        @Override
        public @Nullable Bid poll() {
            return queue.poll(bid -> !cancelledOrders.remove(bid.orderId()));
        }

        public void push(@NotNull Bid bid) {
            queue.push(bid, bid.validFor());
        }
    }

    @Port
    public interface OutputPort {
        void orderCancelled(@NotNull Bid bid);
    }
}
