package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class WeeklyMessageDTO implements DTOEntity, Serializable, Comparable<WeeklyMessageDTO> {

    private String weeklyMessageID, title, subTitle, text, stringDate;
    private HashMap<String, PhotoDTO> photos;
    private HashMap<String, VideoDTO> videos;
    private HashMap<String, PodcastDTO> podcasts;
    private Long date;
    private Integer year, weekNumber;
    private CategoryDTO category;
    private String companyID, companyName, stringDateUpdated, html;
    private boolean active;
    private Long dateUpdated;
    private HashMap<String, String> urls;


    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public HashMap<String, String> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, String> urls) {
        this.urls = urls;
    }


    public HashMap<String, PodcastDTO> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(HashMap<String, PodcastDTO> podcasts) {
        this.podcasts = podcasts;
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

    public String getWeeklyMessageID() {
        return weeklyMessageID;
    }

    public void setWeeklyMessageID(String weeklyMessageID) {
        this.weeklyMessageID = weeklyMessageID;
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

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
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

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public int compareTo(@NonNull WeeklyMessageDTO d) {
        if (date > d.date) {
            return -1;
        }
        if (date < d.date) {
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
        return category.getCategoryName();
    }

    @Override
    public String getBottomText() {
        return stringDate;
    }
}
