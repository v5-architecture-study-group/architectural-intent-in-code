package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;

import java.util.Objects;

@DomainPrimitive
@ThreadSafe
public abstract sealed class OrderId permits BidId, AskId {
    private final long id;

    public OrderId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return Long.toString(id);
    }

    public long toLong() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId orderId = (OrderId) o;
        return id == orderId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
