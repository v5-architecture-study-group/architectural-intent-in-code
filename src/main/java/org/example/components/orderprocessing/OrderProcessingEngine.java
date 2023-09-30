package org.example.components.orderprocessing;

import org.example.components.orderprocessing.domain.model.*;
import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.components.orderprocessing.messages.OrderCompletedEvent;
import org.example.components.orderprocessing.messages.TransactionCreatedEvent;
import org.example.pipesandfilters.MessageSink;
import org.example.stereotype.ActiveWorker;
import org.example.stereotype.ComponentDelegate;
import org.example.stereotype.Event;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

@ThreadSafe
@ComponentDelegate
class OrderProcessingEngine implements ActiveWorker {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingEngine.class);
    private final OrderProcessingWorker worker;
    private final AtomicBoolean terminated = new AtomicBoolean(false);
    private final Thread thread;

    OrderProcessingEngine(@NotNull PrioritizedAskQueue askQueue,
                          @NotNull PrioritizedBidQueue bidQueue,
                          @NotNull TransactionIdFactory transactionIdFactory,
                          @NotNull MessageSink<Event> outputPort) {
        requireNonNull(outputPort);
        worker = new OrderProcessingWorker(askQueue, bidQueue, new OrderProcessingWorkerOutputPort() {
            @Override
            public void writeTransaction(@NotNull Transaction transaction) {
                log.info("Transaction created: {}", transaction);
                outputPort.put(new TransactionCreatedEvent(transaction));
            }

            @Override
            public void notifyOrderCompleted(@NotNull OrderId orderId) {
                log.info("Order completed: {}", orderId);
                outputPort.put(new OrderCompletedEvent(orderId));
            }
        }, transactionIdFactory);
        thread = new Thread(this::run, "OrderProcessingEngine");
    }

    private void run() {
        while (!terminated.get()) {
            try {
                worker.executeNextJob();
            } catch (Throwable ex) {
                log.error("Error while invoking the OrderProcessingWorker", ex);
            }
        }
    }

    @Override
    public void start() {
        try {
            thread.start();
        } catch (IllegalThreadStateException ignoreIt) {
        }
    }

    @Override
    public void shutdown() throws InterruptedException {
        terminated.set(true);
        thread.join();
    }
}
