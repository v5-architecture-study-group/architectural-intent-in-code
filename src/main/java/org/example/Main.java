package org.example;

import org.example.components.marketprice.MarketPriceComponent;
import org.example.components.messagecounter.MessageCounterComponent;
import org.example.components.orderprocessing.OrderProcessingComponent;
import org.example.components.orderprocessing.messages.OrderCancelledEvent;
import org.example.components.orderprocessing.messages.OrderCompletedEvent;
import org.example.components.orderprocessing.messages.TransactionCreatedEvent;
import org.example.components.testdata.TestCommandGeneratorComponent;
import org.example.components.transactionstore.FileSystemTransactionStoreComponent;
import org.example.components.transactionstore.LoggingTransactionStoreErrorHandler;
import org.example.components.transactionstore.TransactionJsonSerializer;
import org.example.pipesandfilters.CastIfInstanceFilter;
import org.example.pipesandfilters.Pipe;
import org.example.stereotype.Event;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        var fileExecutor = Executors.newSingleThreadExecutor();

        var testCommandGenerator = new TestCommandGeneratorComponent();
        var orderProcessing = new OrderProcessingComponent();
        var transactionStore = new FileSystemTransactionStoreComponent(
                new TransactionJsonSerializer(),
                new File("transactions"),
                fileExecutor,
                LoggingTransactionStoreErrorHandler.singleton()
        );
        var marketPrice = new MarketPriceComponent();

        var createdTransactions = CastIfInstanceFilter.<Event, TransactionCreatedEvent>castIfInstanceOf(TransactionCreatedEvent.class);
        var cancelledOrders = CastIfInstanceFilter.<Event, OrderCancelledEvent>castIfInstanceOf(OrderCancelledEvent.class);
        var completedOrders = CastIfInstanceFilter.<Event, OrderCompletedEvent>castIfInstanceOf(OrderCompletedEvent.class);

        var submittedOrderCounter = new MessageCounterComponent();
        var cancelledOrderCounter = new MessageCounterComponent();
        var completedOrderCounter = new MessageCounterComponent();

        Pipe.connect(testCommandGenerator.output(), orderProcessing.input());
        Pipe.connect(testCommandGenerator.output(), submittedOrderCounter.input());

        Pipe.connect(orderProcessing.output(), createdTransactions.input());
        Pipe.connect(orderProcessing.output(), cancelledOrders.input());
        Pipe.connect(orderProcessing.output(), completedOrders.input());

        Pipe.connect(createdTransactions.output(), transactionStore.input());
        Pipe.connect(createdTransactions.output(), marketPrice.input());

        Pipe.connect(cancelledOrders.output(), cancelledOrderCounter.input());

        Pipe.connect(completedOrders.output(), completedOrderCounter.input());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                testCommandGenerator.shutdown();
                orderProcessing.shutdown();
                fileExecutor.shutdown();
                fileExecutor.awaitTermination(10, TimeUnit.SECONDS);

                System.out.println("Submitted orders: " + submittedOrderCounter.count());
                System.out.println("Cancelled orders: " + cancelledOrderCounter.count());
                System.out.println("Completed orders: " + completedOrderCounter.count());
                testCommandGenerator.exampleStocks().forEach(stock -> System.out.println("Market price for " + stock + ": " + marketPrice.getMarketPrice(stock).orElse(null)));
            } catch (InterruptedException ignoreIt) {
            }
        }));

        orderProcessing.start();
        testCommandGenerator.start();

        System.out.println("Press Ctrl+C to exit");
        Thread.sleep(Long.MAX_VALUE);
    }
}