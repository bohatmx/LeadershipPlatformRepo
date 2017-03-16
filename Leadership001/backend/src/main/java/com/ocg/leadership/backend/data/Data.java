package com.ocg.leadership.backend.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 11/9/16.
 */

public class Data implements Serializable{
    private String fromUser, message, title,
            userID, json;
    private Long date, expiryDate;
    private int messageType;

    public static final int DAILY = 101, WEEKLY = 102, INCIDENT = 103,
    WELCOME = 104, OTHER = 105;

    public Data() {
    }


    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }
}
