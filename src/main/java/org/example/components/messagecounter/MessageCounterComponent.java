package org.example.components.messagecounter;

import org.example.pipesandfilters.HasInput;
import org.example.pipesandfilters.MessageSink;
import org.example.stereotype.Component;
import org.example.stereotype.Message;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

@Component
@ThreadSafe
public final class MessageCounterComponent implements HasInput<Message> {

    private final AtomicLong counter = new AtomicLong(0);

    private void onMessage(@NotNull Message message) {
        counter.incrementAndGet();
    }

    public long count() {
        return counter.get();
    }

    @Override
    public @NotNull MessageSink<Message> input() {
        return this::onMessage;
    }
}
