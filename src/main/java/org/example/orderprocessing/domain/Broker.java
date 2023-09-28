package org.example.orderprocessing.domain;

import org.example.stereotype.ThreadSafe;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public final class Broker {
    private final String name;

    public Broker(String name) {
        this.name = requireNonNull(name);
        // TODO Validate name
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
