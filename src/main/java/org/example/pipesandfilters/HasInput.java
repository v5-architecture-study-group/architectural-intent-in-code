package org.example.pipesandfilters;

import org.example.stereotype.Message;
import org.jetbrains.annotations.NotNull;

public interface HasInput<M extends Message> {
    @NotNull MessageSink<M> input();
}
