package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class WeeklyMasterClassDTO extends BaseDTO implements  Serializable, Comparable<WeeklyMasterClassDTO> {
    private String weeklyMasterClassID, text, stringDate, address;
    private Double latitude, longitude;

    private Long date;
    private Integer year, weekNumber;
    private HashMap<String, CategoryDTO> categories;
    private String  stringDateUpdated, html, thumbnailUrl;
    private boolean active;
    private Long dateUpdated;
    private HashMap<String, PhotoDTO> photos;
    private HashMap<String, VideoDTO> videos;
    private HashMap<String,PodcastDTO> podcasts;
    private HashMap<String,EBookDTO> ebooks;
    private HashMap<String,UrlDTO> urls;
    private HashMap<String,CalendarEventDTO> calendarEvents;
    private HashMap<String,RatingDTO> ratings;

    public HashMap<String, RatingDTO> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, RatingDTO> ratings) {
        this.ratings = ratings;
    }


    public HashMap<String, CalendarEventDTO> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(HashMap<String, CalendarEventDTO> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public HashMap<String, UrlDTO> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, UrlDTO> urls) {
        this.urls = urls;
    }

    public HashMap<String, EBookDTO> getEbooks() {
        return ebooks;
    }

    public void setEbooks(HashMap<String, EBookDTO> ebooks) {
        this.ebooks = ebooks;
    }


    public HashMap<String, PodcastDTO> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(HashMap<String, PodcastDTO> podcasts) {
        this.podcasts = podcasts;
    }

    public HashMap<String, PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(HashMap<String, PhotoDTO> photos) {
        this.photos = photos;
    }

    public HashMap<String, VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(HashMap<String, VideoDTO> videos) {
        this.videos = videos;
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public HashMap<String, CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, CategoryDTO> categories) {
        this.categories = categories;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getStringDateUpdated() {
        return stringDateUpdated;
    }

    public void setStringDateUpdated(String stringDateUpdated) {
        this.stringDateUpdated = stringDateUpdated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }


    public String getWeeklyMasterClassID() {
        return weeklyMasterClassID;
    }

    public void setWeeklyMasterClassID(String weeklyMasterClassID) {
        this.weeklyMasterClassID = weeklyMasterClassID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    @Exclude
    public int compareTo(@NonNull WeeklyMasterClassDTO d) {
        if (date > d.date) {
            return -1;
        }
        if (date < d.date) {
            return 1;
        }
        return 0;

    }

    @Override
    public void setJournalUserID(String userID) {

    }

    @Override
    public void setJournalUserName(String userName) {

    }

    public String getStringDateScheduled() {
        return stringDateScheduled;
    }

    public void setStringDateScheduled(String stringDateScheduled) {
        this.stringDateScheduled = stringDateScheduled;
    }

    public Long getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(Long dateScheduled) {
        stringDateScheduled = sdf.format(new Date(dateScheduled));
        this.dateScheduled = dateScheduled;
    }

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
