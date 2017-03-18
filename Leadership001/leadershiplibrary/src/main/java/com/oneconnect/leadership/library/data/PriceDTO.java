package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class PriceDTO implements DTOEntity, Serializable, Comparable<PriceDTO>{

    private String priceID, productName, stringDate, dailyThoughtID,
            weeklyMessageID, weeklyMasterclassID, ebookID;
    private Long date;
    private Double amount;
    private String companyID, companyName, stringDateUpdated;
    private boolean active;
    private Long dateUpdated;


    public String getDailyThoughtID() {
        return dailyThoughtID;
    }

    public void setDailyThoughtID(String dailyThoughtID) {
        this.dailyThoughtID = dailyThoughtID;
    }

    public String getWeeklyMessageID() {
        return weeklyMessageID;
    }

    public String getEbookID() {
        return ebookID;
    }

    public void setEbookID(String ebookID) {
        this.ebookID = ebookID;
    }

    public void setWeeklyMessageID(String weeklyMessageID) {
        this.weeklyMessageID = weeklyMessageID;
    }

    public String getWeeklyMasterclassID() {
        return weeklyMasterclassID;
    }

    public void setWeeklyMasterclassID(String weeklyMasterclassID) {
        this.weeklyMasterclassID = weeklyMasterclassID;
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


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPriceID() {
        return priceID;
    }

    public void setPriceID(String priceID) {
        this.priceID = priceID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
    @Override
    public int compareTo(@NonNull PriceDTO d) {
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
        return null;
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
