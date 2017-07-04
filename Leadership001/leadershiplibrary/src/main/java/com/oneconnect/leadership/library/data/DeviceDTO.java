package com.oneconnect.leadership.library.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 11/11/16.
 */

public class DeviceDTO extends BaseDTO implements Serializable{

    private String model, manufacturer, androidVersion, serialNumber,
            iosVersion, deviceID;
    private String  userID, email,
            firstName,lastName;
    private int userType;
    private String token;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");

    public DeviceDTO() {

    }

    @Override
    public void setJournalUserID(String userID) {

    }

    @Override
    public void setJournalUserName(String userName) {

    }

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

}
