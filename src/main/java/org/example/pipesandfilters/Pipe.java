package org.example.pipesandfilters;

import org.example.stereotype.Message;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

@ThreadSafe
public final class Pipe<M extends Message> {
    private final Subscription subscription;

    public Pipe(@NotNull MessageSource<M> source, @NotNull MessageSink<? super M> sink) {
        subscription = source.subscribe(sink::put);
    }

    public void disconnect() {
        subscription.unsubscribe();
    }

    public static <M extends Message> @NotNull Pipe<M> connect(@NotNull MessageSource<M> source,
                                                               @NotNull MessageSink<? super M> sink) {
        return new Pipe<>(source, sink);
    }
}
