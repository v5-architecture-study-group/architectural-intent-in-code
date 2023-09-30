package org.example.components.transactionstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.components.orderprocessing.domain.model.Transaction;
import org.example.stereotype.ComponentDelegate;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

@ComponentDelegate
@ThreadSafe
public final class TransactionJsonSerializer implements TransactionSerializer {
    private final JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(true);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void serialize(@NotNull Transaction transaction, @NotNull OutputStream out) throws IOException {
        var root = jsonNodeFactory.objectNode();
        root.put("id", transaction.transactionId().toString());
        root.put("stock", transaction.stock().toString());
        root.put("bidder", transaction.bidder().toString());
        root.put("bidId", transaction.bidId().toLong());
        root.put("seller", transaction.seller().toString());
        root.put("askId", transaction.askId().toLong());
        root.put("shares", transaction.shares().toInt());

        var pricePerShare = root.putObject("pricePerShare");
        pricePerShare.put("currency", transaction.pricePerShare().currency().getCurrencyCode());
        pricePerShare.put("amount", transaction.pricePerShare().amount());

        objectMapper.writeValue(out, root);
    }
}
