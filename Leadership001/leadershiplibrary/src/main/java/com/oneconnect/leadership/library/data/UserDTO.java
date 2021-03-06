package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class UserDTO extends BaseDTO implements Serializable, Comparable<UserDTO> {

    private ClientDTO client;        //not null if user belongs to this client
    public static final int
            SUBSCRIBER = 1,
            COMPANY_STAFF = 2,
            LEADER = 3,
            PLATINUM_USER = 4,
            GOLD_USER = 5,
            COMPANY_ADMIN = 6,
            PLATINUM_ADMIN = 7,
            STANDARD_USER = 8;

    private String userID, firstName, lastName,
            email, cellphone, password, uid,
            userDescription, clientID;
    private int userType;
    public static final String
            DESC_SUBSCRIBER = "Subscriber",
            DESC_STAFF = "Company Staff",
            DESC_LEADER = "Leader",
            DESC_PLATINUM_USER = "Platinum",
            DESC_GOLD_USER = "Gold",
            DESC_COMPANY_ADMIN = "Company Admin",
            DESC_PLATINUM_ADMIN = "Platinum Admin",
            DESC_STANDARD_USER = "Standard";

    private HashMap<String, DeviceDTO> devices;
    private HashMap<String, SubscriptionDTO> subscriptions;
    private HashMap<String, PhotoDTO> photos;
    //null if user subscription via client
    private String filePath, storageName,url, stringDate;
    public UserDTO() {

    }

    public HashMap<String, SubscriptionDTO> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(HashMap<String, SubscriptionDTO> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public HashMap<String, DeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(HashMap<String, DeviceDTO> devices) {
        this.devices = devices;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @Exclude
    public String getFullName() {
        if (firstName == null) return null;
        return firstName.concat(" ").concat(lastName);
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
        switch (userType) {
            case SUBSCRIBER:
                userDescription = DESC_SUBSCRIBER;
                break;
            case LEADER:
                userDescription = DESC_LEADER;
                break;
            case COMPANY_STAFF:
                userDescription = DESC_STAFF;
                break;
            case PLATINUM_USER:
                userDescription = DESC_PLATINUM_USER;
                break;
            case GOLD_USER:
                userDescription = DESC_GOLD_USER;
                break;
            case COMPANY_ADMIN:
                userDescription = DESC_COMPANY_ADMIN;
                break;
            case PLATINUM_ADMIN:
                userDescription = DESC_PLATINUM_ADMIN;
                break;
            case STANDARD_USER:
                userDescription = DESC_STANDARD_USER;
                break;
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    @Override
    public void setJournalUserID(String userID) {
       this.journalUserID = userID;
    }

    @Override
    public void setJournalUserName(String userName) {
        this.journalUserName = userName;
    }

    @Override
    public int compareTo(@NonNull UserDTO u) {
        String n1 = this.lastName.concat(" ").concat(this.firstName);
        String n2 = u.lastName.concat(" ").concat(u.firstName);
        return n1.compareTo(n2);
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

    public HashMap<String, PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(HashMap<String, PhotoDTO> photos) {
        this.photos = photos;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
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
}
