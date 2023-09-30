package org.example.components.testdata;

import org.example.components.orderprocessing.domain.primitives.*;
import org.example.components.orderprocessing.messages.BuyStockCommand;
import org.example.components.orderprocessing.messages.CancelOrderCommand;
import org.example.components.orderprocessing.messages.SellStockCommand;
import org.example.pipesandfilters.HasOutput;
import org.example.pipesandfilters.HotMessageSource;
import org.example.pipesandfilters.MessageSource;
import org.example.stereotype.ActiveWorker;
import org.example.stereotype.Command;
import org.example.stereotype.Component;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ThreadSafe
public final class TestCommandGeneratorComponent implements ActiveWorker, HasOutput<Command> {

    private static final Stock[] STOCKS = {
            new Stock("AAPL"),
            new Stock("MSFT"),
            new Stock("KO"),
            new Stock("PEP"),
            new Stock("IBM"),
            new Stock("ORCL")
    };

    private static final Broker[] BROKERS = {
            new Broker("Joe"),
            new Broker("Alice"),
            new Broker("Bob"),
            new Broker("Eve"),
            new Broker("Jennifer"),
            new Broker("Max")
    };

    private static final Duration COMMAND_DURATION = Duration.ofSeconds(10);

    private final HotMessageSource<Command> output = new HotMessageSource<>();
    private final Random rnd = new Random();
    private final AtomicLong nextOrderId = new AtomicLong(1L);
    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    private final Thread thread = new Thread(this::run, "TestCommandGenerator");
    private final List<OrderId> issuedOrders = new ArrayList<>();

    private void run() {
        while (!shutdown.get()) {
            publishCommand();
            randomSleep();
        }
    }

    private void randomSleep() {
        try {
            Thread.sleep(rnd.nextInt(1000));
        } catch (InterruptedException ignoreIt) {
        }
    }

    private @NotNull Command generateCommand() {
        var broker = BROKERS[rnd.nextInt(BROKERS.length)];
        var stock = STOCKS[rnd.nextInt(BROKERS.length)];
        var shares = new Shares((rnd.nextInt(10) + 1) * 50);
        var price = new PositiveMoney(Currency.getInstance("EUR"), new BigDecimal((rnd.nextInt(5) + 1) * 5));

        if (!issuedOrders.isEmpty()) {
            if (rnd.nextInt(10) == 9) {
                return new CancelOrderCommand(issuedOrders.get(rnd.nextInt(issuedOrders.size())));
            }
        }

        if (rnd.nextBoolean()) {
            var id = createAskId();
            issuedOrders.add(id);
            return new SellStockCommand(
                    id,
                    broker,
                    stock,
                    shares,
                    price,
                    COMMAND_DURATION
            );
        } else {
            var id = createBidId();
            issuedOrders.add(id);
            return new BuyStockCommand(
                    id,
                    broker,
                    stock,
                    shares,
                    price,
                    COMMAND_DURATION
            );
        }
    }

    private @NotNull AskId createAskId() {
        return new AskId(nextOrderId.getAndIncrement());
    }

    private @NotNull BidId createBidId() {
        return new BidId(nextOrderId.getAndIncrement());
    }

    private void publishCommand() {
        output.publish(generateCommand());
    }

    @Override
    public @NotNull MessageSource<Command> output() {
        return output;
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
        shutdown.set(true);
        thread.join();
    }

    public @NotNull List<Stock> exampleStocks() {
        return List.of(STOCKS);
    }
}
