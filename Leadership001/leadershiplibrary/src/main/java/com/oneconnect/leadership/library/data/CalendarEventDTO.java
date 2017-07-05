package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 3/25/17.
 */

public class CalendarEventDTO implements Serializable, Comparable<CalendarEventDTO>{
    private String calendarEventID, description, title,
            podcastID, dailyThoughtID, weeklyMessageID, weeklyMasterclassID,
            eBookID, stringStartDate, stringEndDate, stringDateRegistered,
            arrangedBy, userID, attendees;
    private long startDate, endDate, dateRegistered;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss");

    public CalendarEventDTO() {
        dateRegistered = new Date().getTime();
        stringDateRegistered = sdf.format(new Date());

    }

    public String geteBookID() {
        return eBookID;
    }

    public void seteBookID(String eBookID) {
        this.eBookID = eBookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCalendarEventID() {
        return calendarEventID;
    }

    public void setCalendarEventID(String calendarEventID) {
        this.calendarEventID = calendarEventID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPodcastID() {
        return podcastID;
    }

    public void setPodcastID(String podcastID) {
        this.podcastID = podcastID;
    }

    public String getDailyThoughtID() {
        return dailyThoughtID;
    }

    public void setDailyThoughtID(String dailyThoughtID) {
        this.dailyThoughtID = dailyThoughtID;
    }

    public String getWeeklyMessageID() {
        return weeklyMessageID;
    }

    public void setWeeklyMessageID(String weeklyMessageID) {
        this.weeklyMessageID = weeklyMessageID;
    }

    public String getWeeklyMasterclassID() {
        return weeklyMasterclassID;
    }

    public void setWeeklyMasterclassID(String weeklyMasterclassID) {
        this.weeklyMasterclassID = weeklyMasterclassID;
    }

    public String getStringStartDate() {
        return stringStartDate;
    }

    public void setStringStartDate(String stringStartDate) {
        this.stringStartDate = stringStartDate;
    }

    public String getStringEndDate() {
        return stringEndDate;
    }

    public void setStringEndDate(String stringEndDate) {
        this.stringEndDate = stringEndDate;
    }

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public void setStringDateRegistered(String stringDateRegistered) {
        this.stringDateRegistered = stringDateRegistered;
    }

    public String getArrangedBy() {
        return arrangedBy;
    }

    public void setArrangedBy(String arrangedBy) {
        this.arrangedBy = arrangedBy;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        stringStartDate = sdf.format(new Date(startDate));
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        stringEndDate = sdf.format(new Date(endDate));
        this.endDate = endDate;
    }

    public long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public int compareTo(@NonNull CalendarEventDTO c) {
        if (this.startDate > c.startDate) {
            return -1;
        }
        if (this.startDate < c.startDate) {
            return 1;
        }
        return 0;
    }
}
