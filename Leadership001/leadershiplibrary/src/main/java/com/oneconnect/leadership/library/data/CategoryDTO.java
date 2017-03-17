package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class CategoryDTO implements DTOEntity, Serializable, Comparable<CategoryDTO>{
   private String categoryID, categoryName;
    private String companyID, companyName, stringDateUpdated;
    private boolean active;
    private Long dateUpdated;

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

    @Override
    public int compareTo(@NonNull CategoryDTO c) {
        return this.categoryName.compareTo(c.categoryName);
    }

    @Override
    public String getTitleForAdapter() {
        return categoryName;
    }

    @Override
    public String getTopText() {
        return null;
    }

    @Override
    public String getBottomTitle() {
        return null;
    }

    @Override
    public String getBottomText() {
        return null;
    }
}
