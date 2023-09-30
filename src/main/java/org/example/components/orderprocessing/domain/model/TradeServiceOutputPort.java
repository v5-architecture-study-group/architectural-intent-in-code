package org.example.components.orderprocessing.domain.model;

import org.example.components.orderprocessing.domain.primitives.*;
import org.example.stereotype.Port;
import org.jetbrains.annotations.NotNull;

@Port
interface TradeServiceOutputPort {
    void writeTransaction(@NotNull Stock stock,
                          @NotNull Broker bidder,
                          @NotNull OrderId bidId,
                          @NotNull Broker seller,
                          @NotNull OrderId askId,
                          @NotNull Shares shares,
                          @NotNull PositiveMoney pricePerShare);

    void completeOrder(@NotNull Ask ask);

    void completeOrder(@NotNull Bid bid);
}
