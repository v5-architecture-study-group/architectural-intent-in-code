package org.example.components.orderprocessing.domain.primitives;

import org.example.stereotype.DomainPrimitive;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@DomainPrimitive
@ThreadSafe
public final class Shares {

    private final int shares;

    public Shares(int shares) {
        if (shares <= 0) {
            throw new IllegalArgumentException("Shares must be positive");
        }
        this.shares = shares;
    }

    public @NotNull Shares minus(@NotNull Shares toRemove) {
        return new Shares(shares - toRemove.shares);
    }

    public int toInt() {
        return shares;
    }

    @Override
    public String toString() {
        return Integer.toString(shares);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shares shares1 = (Shares) o;
        return shares == shares1.shares;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shares);
    }

    public static @NotNull Shares min(@NotNull Shares s1, @NotNull Shares s2) {
        return s1.shares < s2.shares ? s1 : s2;
    }
}
