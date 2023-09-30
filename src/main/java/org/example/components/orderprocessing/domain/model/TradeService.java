package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.Shares;
import org.example.stereotype.DomainService;
import org.example.stereotype.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

@DomainService
@NotThreadSafe
final class TradeService {

    private final TradeServiceOutputPort outputPort;

    TradeService(@NotNull TradeServiceOutputPort outputPort) {
        this.outputPort = requireNonNull(outputPort);
    }

    public void processTrade(@NotNull Ask ask, @NotNull Bid bid) {
        var shares = Shares.min(ask.shares(), bid.shares());
        outputPort.writeTransaction(ask.stock(), bid.broker(), bid.orderId(), ask.broker(), ask.orderId(), shares, bid.bidPricePerShare());
        if (shares.equals(ask.shares())) {
            outputPort.completeOrder(ask);
        } else {
            ask.sharesSold(shares);
        }
        if (shares.equals(bid.shares())) {
            outputPort.completeOrder(bid);
        } else {
            bid.sharesBought(shares);
        }
    }
}
