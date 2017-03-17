package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class UserDTO implements DTOEntity, Serializable, Comparable<UserDTO>{

    public static final int
            SUBSCRIBER = 1,
            COMPANY_STAFF = 2,
            LEADER = 3;
    private String userID, firstName, lastName, companyName,
            email, cellphone, password, uid, companyID,
            stringDateRegistered, userDescription;
    private Long dateRegistered;
    private int userType;
    public static final String DESC_SUBSCRIBER = "Subscriber",
    DESC_STAFF = "Company Staff", DESC_LEADER = "Leader";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");

    public UserDTO() {
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

    public void setFullName(String fullName) {
    }
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

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public void setStringDateRegistered(String stringDateRegistered) {
        this.stringDateRegistered = stringDateRegistered;
    }

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public int compareTo(@NonNull UserDTO u) {
        String n1 = this.lastName.concat(" ").concat(this.firstName);
        String n2 = u.lastName.concat(" ").concat(u.firstName);
        return n1.compareTo(n2);
    }

    @Override
    public String getTitleForAdapter() {
        return getFullName();
    }

    @Override
    public String getTopText() {
        return email;
    }

    @Override
    public String getBottomTitle() {
        return stringDateRegistered;
    }

    @Override
    public String getBottomText() {
        return companyName;
    }
}
