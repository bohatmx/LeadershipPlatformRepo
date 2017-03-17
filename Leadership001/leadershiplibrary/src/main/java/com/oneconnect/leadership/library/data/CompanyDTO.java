package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class CompanyDTO implements DTOEntity, Serializable, Comparable<CompanyDTO> {
    private String companyID, companyName, email,
    stringDateRegistered,
    countryID, address;
    private Long dateRegistered;

    public CompanyDTO() {
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public void setStringDateRegistered(String stringDateRegistered) {
        this.stringDateRegistered = stringDateRegistered;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public int compareTo(@NonNull CompanyDTO c) {
        return this.companyName.compareTo(c.companyName);
    }

    @Override
    public String getTitleForAdapter() {
        return companyName;
    }

    @Override
    public String getTopText() {
        return email;
    }

    @Override
    public String getBottomTitle() {
        return address;
    }

    @Override
    public String getBottomText() {
        return stringDateRegistered;
    }
}
