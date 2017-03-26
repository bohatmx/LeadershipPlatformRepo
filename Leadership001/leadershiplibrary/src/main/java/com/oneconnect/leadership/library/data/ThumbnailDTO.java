package com.oneconnect.leadership.library.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 3/24/17.
 */

public class ThumbnailDTO  {
    private String thumbnailID, url, stringDate, companyID, companyName, filePath;
    private long date;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss");

    public ThumbnailDTO() {
        date = new Date().getTime();
        stringDate = sdf.format(new Date());
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getThumbnailID() {
        return thumbnailID;
    }

    public void setThumbnailID(String thumbnailID) {
        this.thumbnailID = thumbnailID;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
