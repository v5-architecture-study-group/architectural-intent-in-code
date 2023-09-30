package org.example.stereotype;

public interface ActiveWorker {
    void start();

    void shutdown() throws InterruptedException;
}
