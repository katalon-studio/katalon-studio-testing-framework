package com.kms.katalon.core.util.internal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final DecimalFormat SECOND_FORMAT = new DecimalFormat("#0.000");
    public static String getElapsedTime(long startTime, long endTime) {
        if (endTime < startTime) {
            return "";
        }
        long totalMillis = endTime - startTime;
        long totalSeconds = totalMillis / 1000;
        long totalMinutes = totalSeconds / 60;

        int elapsedMillis = (int) (totalMillis % 1000);
        double elapsedSeconds = (int) (totalSeconds % 60) +  ((double) elapsedMillis / 1000);
        int elapsedMinutes = (int) (totalMinutes % 60);
        int elapsedHours = (int) (totalMinutes / 60);

        StringBuilder elapsedTimeBuilder = new StringBuilder();
        if (elapsedHours > 0) {
            elapsedTimeBuilder.append(Integer.toString(elapsedHours)).append("h - ");
        }
        
        if (elapsedMinutes > 0) {
            elapsedTimeBuilder.append(Integer.toString(elapsedMinutes)).append("m - ");
        }
        elapsedTimeBuilder.append(SECOND_FORMAT.format(elapsedSeconds)).append("s");
        
        return elapsedTimeBuilder.toString();
    }

    public static String getDateTimeFormatted(long timeValue) {
        Date date = new Date(timeValue);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df2.format(date);
    }
}
