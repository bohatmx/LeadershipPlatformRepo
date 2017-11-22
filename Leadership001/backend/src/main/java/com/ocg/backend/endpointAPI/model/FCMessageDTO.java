package com.ocg.backend.endpointAPI.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.ocg.backend.endpointAPI.model.data.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nkululekochrisskosana on 2017/11/09.
 */
@Entity
public class FCMessageDTO implements Serializable {

    @Id
    Long id;
    @Index
    String companyID;
    @Index String dailyThoughtID;
    @Index String weeklyMasterClassID;
    @Index Long date;
    List<String> userIDs = new ArrayList<>();
    String companyName, title;
    com.ocg.backend.endpointAPI.model.Data data;
    int userType;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getDailyThoughtID() {
        return dailyThoughtID;
    }

    public void setDailyThoughtID(String dailyThoughtID) {
        this.dailyThoughtID = dailyThoughtID;
    }

    public String getWeeklyMasterClassID() {
        return weeklyMasterClassID;
    }

    public void setWeeklyMasterClassID(String weeklyMasterClassID) {
        this.weeklyMasterClassID = weeklyMasterClassID;
    }

    public com.ocg.backend.endpointAPI.model.Data getData() {
        return data;
    }

    public void setData(com.ocg.backend.endpointAPI.model.Data data) {
        this.data = data;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }
}
