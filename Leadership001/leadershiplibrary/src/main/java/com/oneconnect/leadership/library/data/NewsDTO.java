package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 3/17/17.
 */

public class NewsDTO  extends BaseDTO implements  Serializable, Comparable<NewsDTO> {
    private String body, newsID,
            stringArticleDate, html, tag;
    private long  articleDate;
    private HashMap<String, UrlDTO> urls;
    private HashMap<String, PhotoDTO> photos;
    private HashMap<String, VideoDTO> videos;

    private CategoryDTO category;
    private HashMap<String, CategoryDTO> categories;

    public NewsDTO() {
    }

    public HashMap<String, CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, CategoryDTO> categories) {
        this.categories = categories;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

    @Override
    public void setJournalUserID(String userID) {

    }

    @Override
    public void setJournalUserName(String userName) {

    }


    public long getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(long articleDate) {
        this.articleDate = articleDate;
    }

    public HashMap<String, UrlDTO> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, UrlDTO> urls) {
        this.urls = urls;
    }

    /* public HashMap<String, String> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, String> urls) {
        this.urls = urls;
    }
    */

    @Exclude
    public int compareTo(@NonNull NewsDTO d) {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
