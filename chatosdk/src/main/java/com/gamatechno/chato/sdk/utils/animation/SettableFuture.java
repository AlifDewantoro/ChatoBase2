package com.gamatechno.chato.sdk.utils.animation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SettableFuture<T> implements ListenableFuture<T> {
    private boolean canceled;
    private boolean completed;
    private volatile Throwable exception;
    private final List<Listener<T>> listeners = new LinkedList();
    private volatile T result;

    public synchronized boolean cancel(boolean z) {
        boolean z2 = true;
        synchronized (this) {
            if (this.completed || this.canceled) {
                z2 = false;
            } else {
                this.canceled = true;
            }
        }
        return z2;
    }

    public synchronized boolean isCancelled() {
        return this.canceled;
    }

    public synchronized boolean isDone() {
        return this.completed;
    }

    public boolean set(T t) {
        boolean z = true;
        synchronized (this) {
            if (this.completed || this.canceled) {
                z = false;
            } else {
                this.result = t;
                this.completed = true;
                notifyAll();
                notifyAllListeners();
            }
        }
        return z;
    }

    public boolean setException(Throwable th) {
        boolean z = true;
        synchronized (this) {
            if (this.completed || this.canceled) {
                z = false;
            } else {
                this.exception = th;
                this.completed = true;
                notifyAll();
                notifyAllListeners();
            }
        }
        return z;
    }

    public synchronized T get() throws InterruptedException, ExecutionException {
        while (!this.completed) {
            wait();
        }
        if (this.exception != null) {
            throw new ExecutionException(this.exception);
        }
        return this.result;
    }

    public synchronized T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        long currentTimeMillis = System.currentTimeMillis();
        while (!this.completed && System.currentTimeMillis() - currentTimeMillis > timeUnit.toMillis(j)) {
            wait(timeUnit.toMillis(j));
        }
        if (this.completed) {
        } else {
            throw new TimeoutException();
        }
        return get();
    }

    public void addListener(Listener<T> listener) {
        synchronized (this) {
            this.listeners.add(listener);
            if (this.completed) {
                notifyListener(listener);
                return;
            }
        }
    }

    private void notifyAllListeners() {
        List<Listener> linkedList;

        synchronized (this) {
            linkedList = new LinkedList(this.listeners);
        }
        for (Listener notifyListener : linkedList) {
            notifyListener(notifyListener);
        }
    }

    private void notifyListener(Listener<T> listener) {
        if (this.exception != null) {
            listener.onFailure(new ExecutionException(this.exception));
        } else {
            listener.onSuccess(this.result);
        }
    }
}
