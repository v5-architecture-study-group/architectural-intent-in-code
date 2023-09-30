package org.example.pipesandfilters;

import org.example.stereotype.Message;
import org.example.stereotype.Port;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Port
public interface MessageSource<M extends Message> {
    @NotNull Subscription subscribe(@NotNull Consumer<M> subscriber);
}
