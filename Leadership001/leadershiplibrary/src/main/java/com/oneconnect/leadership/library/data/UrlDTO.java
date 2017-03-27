package com.oneconnect.leadership.library.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 3/26/17.
 */

public class UrlDTO implements Serializable{
    private String title, url, companyID, companyName, stringDateRegistered;
    private long dateRegistered;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss");

    public UrlDTO() {
        dateRegistered = new Date().getTime();
        stringDateRegistered = sdf.format(new Date());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
