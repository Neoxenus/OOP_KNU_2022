package com.my;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPool implements Executor {
    private final Thread[] threads;
    private final AtomicBoolean toShutdown;
    private final BlockingQueue<Runnable> tasks;


    public ThreadPool(int threadsCount) {
        this.threads = new Thread[threadsCount];
        this.toShutdown = new AtomicBoolean(false);
        this.tasks = new LinkedBlockingQueue<>();

        for (int i = 0; i < threadsCount; i++) {
            threads[i] = new Thread(new Worker());
            threads[i].start();
        }

    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!toShutdown.get() || !tasks.isEmpty()) {
                Runnable task = tasks.poll();
                if (task != null)
                    task.run();
            }
        }
    }


    @Override
    public void execute(Runnable command) {
        if (!toShutdown.get()) {
            try {
                tasks.put(command);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void shutdown() {
        toShutdown.set(true);
    }

    public void shutdownNow() {
        toShutdown.set(true);

        for (Thread thread : threads) {
            thread.interrupt();
        }

        tasks.clear();
    }
}