package org.example.pipesandfilters;

import org.example.stereotype.Message;

public interface Filter<Input extends Message, Output extends Message> extends HasInput<Input>, HasOutput<Output> {
}
