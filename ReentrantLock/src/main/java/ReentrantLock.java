class ReentrantLock {
    private final Object sync = new Object(); // private monitor
    private Thread lockedBy = null;  // null => unlocked                        
    private int lockCount = 0;

    public void lock() throws InterruptedException {
        synchronized (sync) {
            Thread callingThread = Thread.currentThread();
            while (lockedBy != null && lockedBy != callingThread)
                wait();
            lockedBy = callingThread; // (re)locked!                            
            lockCount++;
        }
    }

    public void unlock() {
        synchronized (sync) {
            if (Thread.currentThread() == lockedBy)
                if (--lockCount == 0) {
                    lockedBy = null;      // unlocked!                          
                    notify();
                }
        }
    }
}