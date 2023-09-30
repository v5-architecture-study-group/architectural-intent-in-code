package org.example.pipesandfilters;

import org.example.stereotype.Message;
import org.example.stereotype.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

@ThreadSafe
public final class CastIfInstanceFilter<Input extends Message, Output extends Input> implements Filter<Input, Output> {
    private final HotMessageSource<Output> output = new HotMessageSource<>();
    private final Class<Output> outputClass;

    public CastIfInstanceFilter(@NotNull Class<Output> outputClass) {
        this.outputClass = requireNonNull(outputClass);
    }

    private void onInput(@NotNull Input input) {
        if (outputClass.isInstance(input)) {
            output.publish(outputClass.cast(input));
        }
    }

    @Override
    public @NotNull MessageSink<Input> input() {
        return this::onInput;
    }

    @Override
    public @NotNull MessageSource<Output> output() {
        return output;
    }

    public static <Input extends Message, Output extends Input> @NotNull Filter<Input, Output> castIfInstanceOf(@NotNull Class<Output> outputClass) {
        return new CastIfInstanceFilter<>(outputClass);
    }
}
