package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ThreadSafe
@DomainPrimitive
public final class Broker {
    private final String name;

    public Broker(@NotNull String name) {
        this.name = requireNonNull(name);
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Broker broker = (Broker) o;
        return Objects.equals(name, broker.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
