package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.*;
import org.example.stereotype.DomainService;
import org.example.stereotype.NotThreadSafe;
import org.example.stereotype.PassiveWorker;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

@DomainService
@NotThreadSafe
public final class OrderProcessingWorker implements PassiveWorker {

    private final PrioritizedAskQueue askQueue;
    private final PrioritizedBidQueue bidQueue;
    private final TradeService tradeService;

    public OrderProcessingWorker(@NotNull PrioritizedAskQueue askQueue,
                                 @NotNull PrioritizedBidQueue bidQueue,
                                 @NotNull OrderProcessingWorkerOutputPort outputPort,
                                 @NotNull TransactionIdFactory transactionIdFactory) {
        this.askQueue = requireNonNull(askQueue);
        this.bidQueue = requireNonNull(bidQueue);
        requireNonNull(outputPort);
        requireNonNull(transactionIdFactory);
        this.tradeService = new TradeService(new TradeServiceOutputPort() {
            @Override
            public void writeTransaction(@NotNull Stock stock,
                                         @NotNull Broker bidder,
                                         @NotNull OrderId bidId,
                                         @NotNull Broker seller,
                                         @NotNull OrderId askId,
                                         @NotNull Shares shares,
                                         @NotNull PositiveMoney pricePerShare) {
                outputPort.writeTransaction(
                        new Transaction(transactionIdFactory.createNewTransactionId(askId, bidId),
                                stock, bidder, bidId, seller, askId, shares, pricePerShare)
                );
            }

            @Override
            public void completeOrder(@NotNull Ask ask) {
                askQueue.complete(ask);
                outputPort.notifyOrderProcessed(ask.orderId());
            }

            @Override
            public void completeOrder(@NotNull Bid bid) {
                bidQueue.complete(bid);
                outputPort.notifyOrderProcessed(bid.orderId());
            }
        });
    }

    @Override
    public void executeNextJob() {
        bidQueue.getNextBid().ifPresent(bid -> askQueue.getBestAsk(bid).ifPresent(ask -> tradeService.processTrade(ask, bid)));
    }
}
