package org.example.components.orderprocessing.domain.model.queues;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

final class NoLock implements Lock {

    public static final Lock INSTANCE = new NoLock();

    private NoLock() {
    }

    @Override
    public void lock() {
        // NOP
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        // NOP
    }

    @Override
    public boolean tryLock() {
        return true;
    }

    @Override
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        return true;
    }

    @Override
    public void unlock() {
        // NOP
    }

    @NotNull
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
