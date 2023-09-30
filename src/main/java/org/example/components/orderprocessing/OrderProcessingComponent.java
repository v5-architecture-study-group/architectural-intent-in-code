package org.example.components.orderprocessing;

import org.example.components.orderprocessing.domain.model.Ask;
import org.example.components.orderprocessing.domain.model.Bid;
import org.example.components.orderprocessing.domain.model.PrioritizedAskQueue;
import org.example.components.orderprocessing.domain.model.PrioritizedBidQueue;
import org.example.components.orderprocessing.domain.model.factories.DefaultTransactionIdFactory;
import org.example.components.orderprocessing.domain.model.queues.BestTimePrioritizedAskQueue;
import org.example.components.orderprocessing.domain.model.queues.BestTimePrioritizedBidQueue;
import org.example.components.orderprocessing.domain.primitives.AskId;
import org.example.components.orderprocessing.domain.primitives.BidId;
import org.example.components.orderprocessing.domain.primitives.OrderId;
import org.example.components.orderprocessing.messages.BuyStockCommand;
import org.example.components.orderprocessing.messages.CancelOrderCommand;
import org.example.components.orderprocessing.messages.OrderCancelledEvent;
import org.example.components.orderprocessing.messages.SellStockCommand;
import org.example.pipesandfilters.Filter;
import org.example.pipesandfilters.HotMessageSource;
import org.example.pipesandfilters.MessageSink;
import org.example.pipesandfilters.MessageSource;
import org.example.stereotype.ActiveWorker;
import org.example.stereotype.Command;
import org.example.stereotype.Component;
import org.example.stereotype.Event;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OrderProcessingComponent implements Filter<Command, Event>, ActiveWorker {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingComponent.class);
    private static final int QUEUE_CAPACITY = 1024;
    private final PrioritizedAskQueue askQueue;
    private final PrioritizedBidQueue bidQueue;
    private final OrderProcessingEngine engine;
    private final HotMessageSource<Event> output = new HotMessageSource<>();

    public OrderProcessingComponent() {
        askQueue = new BestTimePrioritizedAskQueue(QUEUE_CAPACITY);
        bidQueue = new BestTimePrioritizedBidQueue(QUEUE_CAPACITY);
        engine = new OrderProcessingEngine(askQueue, bidQueue, new DefaultTransactionIdFactory(), output::publish);
    }

    @Override
    public @NotNull MessageSink<Command> input() {
        return this::onCommand;
    }

    private void onCommand(@NotNull Command command) {
        switch (command) {
            case CancelOrderCommand coc -> cancel(coc.orderId());
            case BuyStockCommand bsc -> buy(bsc);
            case SellStockCommand ssc -> sell(ssc);
            default -> throw new IllegalArgumentException("Unknown command");
        }
    }

    private void cancel(@NotNull OrderId orderId) {
        log.info("Cancelling order {}", orderId);
        switch (orderId) {
            case AskId askId -> askQueue.cancel(askId);
            case BidId bidId -> bidQueue.cancel(bidId);
        }
        output.publish(new OrderCancelledEvent(orderId));
    }

    private void buy(@NotNull BuyStockCommand command) {
        log.info("Submitting order {}", command);
        var bid = new Bid(
                command.orderId(),
                command.broker(),
                command.stock(),
                command.validFor(),
                command.bidPricePerShare(),
                command.shares()
        );
        bidQueue.push(bid);
    }

    private void sell(@NotNull SellStockCommand command) {
        log.info("Submitting order {}", command);
        var ask = new Ask(
                command.orderId(),
                command.broker(),
                command.stock(),
                command.validFor(),
                command.askPricePerShare(),
                command.shares()
        );
        askQueue.push(ask);
    }

    @Override
    public @NotNull MessageSource<Event> output() {
        return output;
    }

    @Override
    public void start() {
        engine.start();
    }

    @Override
    public void shutdown() throws InterruptedException {
        engine.shutdown();
    }
}
