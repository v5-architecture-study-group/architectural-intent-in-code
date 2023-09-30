package org.example.pipesandfilters;

import org.example.stereotype.Message;
import org.jetbrains.annotations.NotNull;

public interface HasOutput<M extends Message> {
    @NotNull MessageSource<M> output();
}
