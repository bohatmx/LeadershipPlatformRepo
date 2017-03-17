package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class EBookDTO implements DTOEntity, Serializable, Comparable<EBookDTO>{
    private String eBookID, title, description, url, stringDate;
    private Long date;
    private Integer numberOfPages;
    private String companyID, companyName, stringDateUpdated, html;
    private boolean active;
    private Long dateUpdated;
    private String weeklyMasterClassID, weeklyMessageID, dailyThoughtID,  subjectTitle;

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

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
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

    @Override
    public int compareTo(@NonNull EBookDTO e) {
        return this.title.compareTo(e.title);
    }

    @Override
    public String getTitleForAdapter() {
        return title;
    }

    @Override
    public String getTopText() {
        return description;
    }

    @Override
    public String getBottomTitle() {
        return companyName;
    }

    @Override
    public String getBottomText() {
        return stringDate;
    }
}
