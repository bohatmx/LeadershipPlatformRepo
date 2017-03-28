package com.oneconnect.leadership.library.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 3/17/17.
 */

public class SubscriptionDTO  extends BaseDTO implements  Serializable {
    private String subscriptionTypeID, userID, clientID,
             stringPaymentDate, subscriptionID;
    private long paymentDate;
    private double amount;
    private boolean active;
    private SubscriptionTypeDTO subscriptionType;
    private ClientDTO client;
    private UserDTO user;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyy HH:mm:ss");

    public SubscriptionDTO() {
        dateRegistered = new Date().getTime();
        stringDateRegistered = sdf.format(new Date());
        active = false;
    }

    public String getSubscriptionTypeID() {
        return subscriptionTypeID;
    }

    public void setSubscriptionTypeID(String subscriptionTypeID) {
        this.subscriptionTypeID = subscriptionTypeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getStringPaymentDate() {
        return stringPaymentDate;
    }

    public void setStringPaymentDate(String stringPaymentDate) {
        this.stringPaymentDate = stringPaymentDate;
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(long paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SubscriptionTypeDTO getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionTypeDTO subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public void setJournalUserID(String userID) {

    }

    @Override
    public void setJournalUserName(String userName) {

    }


}
