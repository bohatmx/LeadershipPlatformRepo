package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class EBookDTO  extends BaseDTO implements  Serializable, Comparable<EBookDTO>{
    private String eBookID,  description, url, stringDate;
    private Long date;
    private Integer numberOfPages;
    private String stringDateUpdated, html;
    private boolean active;
    private Long dateUpdated;
    private String weeklyMasterClassID, weeklyMessageID, dailyThoughtID, podcastID;
    private HashMap<String,UrlDTO> urls;
    private HashMap<String, PhotoDTO> photos;
    private HashMap<String, VideoDTO> videos;
    private HashMap<String,PodcastDTO> podcasts;

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

    public HashMap<String, PodcastDTO> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(HashMap<String, PodcastDTO> podcasts) {
        this.podcasts = podcasts;
    }

    public HashMap<String, UrlDTO> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, UrlDTO> urls) {
        this.urls = urls;
    }

    public String getPodcastID() {
        return podcastID;
    }

    public void setPodcastID(String podcastID) {
        this.podcastID = podcastID;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getWeeklyMasterClassID() {
        return weeklyMasterClassID;
    }

    public void setWeeklyMasterClassID(String weeklyMasterClassID) {
        this.weeklyMasterClassID = weeklyMasterClassID;
    }

    public String getWeeklyMessageID() {
        return weeklyMessageID;
    }

    public void setWeeklyMessageID(String weeklyMessageID) {
        this.weeklyMessageID = weeklyMessageID;
    }

    public String getDailyThoughtID() {
        return dailyThoughtID;
    }

    public void setDailyThoughtID(String dailyThoughtID) {
        this.dailyThoughtID = dailyThoughtID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String subjectTitle) {
        this.title = title;
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


    public String geteBookID() {
        return eBookID;
    }

    public void seteBookID(String eBookID) {
        this.eBookID = eBookID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Exclude
    public int compareTo(@NonNull EBookDTO e) {
        return this.title.compareTo(e.title);
    }

    @Exclude
    public String getLine1() {
        return title;
    }

    @Exclude
    public String getLine2() {
        return description;
    }

    @Exclude
    public String getLine3() {
        return companyName;
    }

    @Exclude
    public String getLine4() {
        return stringDate;
    }

    @Override
    public void setJournalUserID(String userID) {

    }

    @Override
    public void setJournalUserName(String userName) {

    }


}
