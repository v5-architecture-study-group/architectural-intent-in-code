package org.example.components.orderprocessing.domain.model.queues;

import org.example.stereotype.Port;
import org.jetbrains.annotations.NotNull;

@Port
interface EvictingQueueOutputPort<E> {
    void elementEvicted(@NotNull E element);
}
