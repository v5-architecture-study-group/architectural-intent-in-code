package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;

@DomainPrimitive
@ThreadSafe
public final class BidId extends OrderId {
    public BidId(long id) {
        super(id);
    }
}
