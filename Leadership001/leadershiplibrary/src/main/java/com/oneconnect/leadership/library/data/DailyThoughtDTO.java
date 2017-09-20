package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class DailyThoughtDTO extends BaseDTO implements  Serializable, Comparable<DailyThoughtDTO> {

    private String dailyThoughtID,
            text, group;
    private String
            stringDateUpdated, html;
    private boolean active;
    private long dateUpdated;
    private CategoryDTO category;
    private UserDTO user;
    private HashMap<String, CategoryDTO> categories;
    private HashMap<String, PhotoDTO> photos;
    private HashMap<String, VideoDTO> videos;
    private HashMap<String,PodcastDTO> podcasts;
    private HashMap<String,EBookDTO> ebooks;
    private HashMap<String,UrlDTO> urls;
    private HashMap<String,CalendarEventDTO> calendarEvents;
    private HashMap<String,RatingDTO> ratings;
    private HashMap<String,UserDTO> users;

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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

    public HashMap<String, UserDTO> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, UserDTO> users) {
        this.users = users;
    }

    /*public List<VideoDTO> getVideoList() {
            return videoList;
        }

        public void setVideoList(List<VideoDTO> videoList) {
            this.videoList = videoList;
        }

        public List<PhotoDTO> getPhotoList() {
            List<PhotoDTO> list = new ArrayList<>();
            if (*//*photos*//*photoList != null) {
            for (PhotoDTO p: photoList*//*photos.values()*//*) {
                list.add(p);
            }
        }
        return list*//*photos*//*;
        *//*return photoList;*//*
    }

    public void setPhotoList(List<PhotoDTO> photoList) {
        this.photoList = photoList;
    }
*/
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

    public HashMap<String, CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, CategoryDTO> categories) {
        this.categories = categories;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public HashMap<String, UrlDTO> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, UrlDTO> urls) {
        this.urls = urls;
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

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDailyThoughtID() {
        return dailyThoughtID;
    }

    public void setDailyThoughtID(String dailyThoughtID) {
        this.dailyThoughtID = dailyThoughtID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public void setJournalUserID(String userID) {

    }

    @Override
    public void setJournalUserName(String userName) {

    }


    @Exclude
    public int compareTo(@NonNull DailyThoughtDTO d) {
        if (dateRegistered > d.dateRegistered) {
            return -1;
        }
        if (dateRegistered < d.dateRegistered) {
            return 1;
        }
        return 0;
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

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
