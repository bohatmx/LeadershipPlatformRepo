package com.oneconnect.leadership.library.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 3/17/17.
 */

public class NewsDTO {
    private String companyID,companyName,title,body, newsID,
            stringArticleDate, stringDateRegistered;
    private long dateRegistered, articleDate;
    private HashMap<String,String> urls;
    private HashMap<String,PhotoDTO> photos;
    private HashMap<String,VideoDTO> videos;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyy HH:mm:ss");

    public NewsDTO() {
        dateRegistered = new Date().getTime();
        stringDateRegistered = sdf.format(new Date());
    }

    public HashMap<String, VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(HashMap<String, VideoDTO> videos) {
        this.videos = videos;
    }

    public HashMap<String, PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(HashMap<String, PhotoDTO> photos) {
        this.photos = photos;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStringArticleDate() {
        return stringArticleDate;
    }

    public void setStringArticleDate(String stringArticleDate) {
        this.stringArticleDate = stringArticleDate;
    }

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public void setStringDateRegistered(String stringDateRegistered) {
        this.stringDateRegistered = stringDateRegistered;
    }

    public long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public long getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(long articleDate) {
        this.articleDate = articleDate;
    }

    public HashMap<String, String> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, String> urls) {
        this.urls = urls;
    }
}
