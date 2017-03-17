package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class DailyThoughtDTO implements DTOEntity, Serializable, Comparable<DailyThoughtDTO> {

    private String dailyThoughtID, title, subTitle,
            text, stringDateRegistered, categoryID, categoryName;
    private Long dateRegistered;
    private String companyID, companyName, stringDateUpdated, html;
    private boolean active;
    private Long dateUpdated;

    private HashMap<String, PhotoDTO> photos;
    private HashMap<String, VideoDTO> videos;
    private HashMap<String, PodcastDTO> podcasts;
    private HashMap<String, String> urls;

    public HashMap<String, PodcastDTO> getPodcasts() {
        return podcasts;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setPodcasts(HashMap<String, PodcastDTO> podcasts) {
        this.podcasts = podcasts;
    }

    public HashMap<String, String> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, String> urls) {
        this.urls = urls;
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

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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


    public String getDailyThoughtID() {
        return dailyThoughtID;
    }

    public void setDailyThoughtID(String dailyThoughtID) {
        this.dailyThoughtID = dailyThoughtID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public void setStringDateRegistered(String stringDateRegistered) {
        this.stringDateRegistered = stringDateRegistered;
    }

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public int compareTo(@NonNull DailyThoughtDTO d) {
        if (dateRegistered > d.dateRegistered) {
            return -1;
        }
        if (dateRegistered < d.dateRegistered) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getTitleForAdapter() {
        return title;
    }

    @Override
    public String getTopText() {
        return text;
    }

    @Override
    public String getBottomTitle() {
        return categoryName;
    }

    @Override
    public String getBottomText() {
        return stringDateRegistered;
    }
}
