package com.kms.katalon.core.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Debouncer<T> {
    public interface Callback<T> {
        public void call(T key);
    }

    private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);

    private final ConcurrentHashMap<T, TimerTask> delayedMap = new ConcurrentHashMap<T, TimerTask>();

    private final Callback<T> callback;

    private final long interval;

    public Debouncer(Callback<T> callback, long interval) {
        this.callback = callback;
        this.interval = interval;
    }

    public void call(T key) {
        TimerTask task = new TimerTask(key);

        TimerTask prev;
        do {
            prev = delayedMap.putIfAbsent(key, task);
            if (prev == null) {
                sched.schedule(task, interval, TimeUnit.MILLISECONDS);
            }
        } while (prev != null && !prev.extend()); // Exit only if new task was added to map, or existing task was
                                                  // extended successfully
    }

    public void terminate() {
        sched.shutdownNow();
    }

    public boolean isStopped() {
        return sched.isShutdown();
    }

    // The task that wakes up when the wait time elapses
    private class TimerTask implements Runnable {
        private final T key;

        private long dueTime;

        private final Object lock = new Object();

        public TimerTask(T key) {
            this.key = key;
            extend();
        }

        public boolean extend() {
            synchronized (lock) {
                if (dueTime < 0) // Task has been shutdown
                    return false;
                dueTime = System.currentTimeMillis() + interval;
                return true;
            }
        }

        public void run() {
            synchronized (lock) {
                long remaining = dueTime - System.currentTimeMillis();
                if (remaining > 0) { // Re-schedule task
                    sched.schedule(this, remaining, TimeUnit.MILLISECONDS);
                } else { // Mark as terminated and invoke callback
                    dueTime = -1;
                    try {
                        callback.call(key);
                    } finally {
                        delayedMap.remove(key);
                    }
                }
            }
        }
    }
}
