package org.example.components.orderprocessing.domain.model;

import org.example.stereotype.Port;
import org.jetbrains.annotations.NotNull;

@Port
public interface OrderProcessingWorkerOutputPort {
    void writeTransaction(@NotNull Transaction transaction);
}
