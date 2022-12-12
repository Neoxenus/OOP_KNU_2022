package org.example;

public class CustomPhaser {
    private int parties;
    private int phase;
    private int partiesAwait;

    public CustomPhaser() {
        this(0);
    }

    public CustomPhaser(int parties) {
        this.parties = parties;
        this.phase = 0;
        this.partiesAwait = parties;
    }

    public synchronized int register() {
        parties++;
        partiesAwait++;
        return phase;
    }

    public synchronized int arriveAndAwaitAdvance() throws InterruptedException {
        partiesAwait--;

        if (partiesAwait > 0) {
            wait();
        }

        notifyAll();
        partiesAwait = parties;
        phase++;

        return phase;
    }

    public synchronized int arriveAndDeregister() {
        partiesAwait--;
        parties--;

        if (partiesAwait == 0) {
            notifyAll();
            phase++;
            partiesAwait = parties;
        }

        return phase;
    }
}