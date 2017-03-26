package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 3/17/17.
 */

public class SubscriptionDTO  extends BaseDTO implements  Serializable, Comparable<SubscriptionDTO> {
    private String userID, firstName, lastName, email,
             stringPaymentDate, description, subscriptionID;
    private long paymentDate;
    private double amount;
    private boolean active;
    private int subscriptionType, periodInMonths;
    private PriceDTO price;
    public static final int
            DAILY_THOUGHT = 1,
            WEEKLY_MESSAGE = 2,
            WEEKLY_MASTERCLASS = 3;
    public static final String
            DESC_DAILY_THOUGHT = "Daily Thought",
            DESC_WEEKLY_MESSAGE = "Weekly Message",
            DESC_WEEKLY_MASTERCLASS = "Weekly MasterClass";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyy HH:mm:ss");

    public SubscriptionDTO() {
        dateRegistered = new Date().getTime();
        stringDateRegistered = sdf.format(new Date());
        active = false;
    }

    public PriceDTO getPrice() {
        return price;
    }

    public void setPrice(PriceDTO price) {
        this.price = price;
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public int getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(int periodInMonths) {
        this.periodInMonths = periodInMonths;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(int subscriptionType) {
        switch (subscriptionType) {
            case DAILY_THOUGHT:
                description = DESC_DAILY_THOUGHT;
                break;
            case WEEKLY_MESSAGE:
                description = DESC_WEEKLY_MESSAGE;
                break;
            case WEEKLY_MASTERCLASS:
                description = DESC_WEEKLY_MASTERCLASS;
                break;

        }
        this.subscriptionType = subscriptionType;
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

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public void setStringDateRegistered(String stringDateRegistered) {
        this.stringDateRegistered = stringDateRegistered;
    }

    public String getStringPaymentDate() {
        return stringPaymentDate;
    }

    public void setStringPaymentDate(String stringPaymentDate) {
        this.stringPaymentDate = stringPaymentDate;
    }

    @Override
    public void setJournalUserID(String userID) {

    }

    @Override
    public void setJournalUserName(String userName) {

    }


    public long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(long paymentDate) {
        this.paymentDate = paymentDate;
        stringPaymentDate = sdf.format(new Date(paymentDate));
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

    @Exclude
    public int compareTo(@NonNull SubscriptionDTO d) {
        if (dateRegistered > d.dateRegistered) {
            return -1;
        }
        if (dateRegistered < d.dateRegistered) {
            return 1;
        }
        return 0;
    }

    @Exclude
    public String getLine1() {
        return description;
    }

    @Exclude
    public String getLine2() {
        return stringPaymentDate;
    }

    @Exclude
    public String getLine3() {
        return String.valueOf(amount);
    }

    @Exclude
    public String getLine4() {
        return stringDateRegistered;
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
