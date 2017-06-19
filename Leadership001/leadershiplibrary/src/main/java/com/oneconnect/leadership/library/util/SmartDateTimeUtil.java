package com.oneconnect.leadership.library.util;

/**
 * Created by Kurisani on 2017/06/12.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class SmartDateTimeUtil {
    private static String getHourMinuteString(Date date){
        SimpleDateFormat hourMinuteFormat = new SimpleDateFormat(" h:m a");
        return hourMinuteFormat.format(date);
    }

    private static String getDateString(Date date){
        SimpleDateFormat dateStringFormat = new SimpleDateFormat("EEE',' MMM d y',' h:m a");
        return dateStringFormat.format(date);
    }

    private static boolean isToday (DateTime dateTime) {
        DateMidnight today = new DateMidnight();
        return today.equals(dateTime.toDateMidnight());
    }

    private static boolean isYesterday (DateTime dateTime) {
        DateMidnight yesterday = (new DateMidnight()).minusDays(1);
        return yesterday.equals(dateTime.toDateMidnight());
    }

    private static boolean isTomorrow(DateTime dateTime){
        DateMidnight tomorrow = (new DateMidnight()).plusDays(1);
        return tomorrow.equals(dateTime.toDateMidnight());
    }
    private static String getDayString(Date date) {
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EEE',' h:m a");
        String s;
        if (isToday(new DateTime(date)))
            s = "Today";
        else if (isYesterday(new DateTime(date)))
            s = "Yesterday," + getHourMinuteString(date);
        else if(isTomorrow(new DateTime(date)))
            s = "Tomorrow," +getHourMinuteString(date);
        else
            s = weekdayFormat.format(date);
        return s;
    }

    public static String getDateString_shortAndSmart(Date date) {
        String s;
        DateTime nowDT = new DateTime();
        DateTime dateDT = new DateTime(date);
        int days = Days.daysBetween(dateDT, nowDT).getDays();
        if (isToday(new DateTime(date)))
            s = "Today,"+getHourMinuteString(date);
        else if (days < 7)
            s = getDayString(date);
        else
            s = getDateString(date);
        return s;
    }

}
