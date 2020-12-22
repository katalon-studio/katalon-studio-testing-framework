package com.kms.katalon.core.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public interface TimerCallback {
        void call();
    }

    public static Timer setInterval(TimerCallback callback, long interval) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callback.call();
            }
        }, interval, interval);
        return timer;
    }

    public static Timer setTimeout(TimerCallback callback, long timeout) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callback.call();
            }
        }, timeout);
        return timer;
    }

    /**
     * Get a diff between two dates
     * 
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
