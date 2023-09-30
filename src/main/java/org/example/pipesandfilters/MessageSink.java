package org.example.pipesandfilters;

import org.example.stereotype.Message;
import org.example.stereotype.Port;
import org.jetbrains.annotations.NotNull;

@Port
public interface MessageSink<M extends Message> {
    void put(@NotNull M message);
}
