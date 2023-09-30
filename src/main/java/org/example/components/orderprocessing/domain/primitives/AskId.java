package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;

@DomainPrimitive
@ThreadSafe
public final class AskId extends OrderId {
    public AskId(long id) {
        super(id);
    }
}
