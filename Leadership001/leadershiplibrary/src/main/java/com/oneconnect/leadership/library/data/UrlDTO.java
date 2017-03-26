package com.oneconnect.leadership.library.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 3/26/17.
 */

public class UrlDTO implements Serializable{
    private String title, url;

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
