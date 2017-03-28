package com.oneconnect.leadership.library.data;

/**
 * Created by aubreymalabie on 3/28/17.
 */

public class SubscriptionTypeDTO {
    private String subscriptionTypeID, subscriptionName;
    private double amount;
    private int periodInMonths;

    public String getSubscriptionTypeID() {
        return subscriptionTypeID;
    }

    public void setSubscriptionTypeID(String subscriptionTypeID) {
        this.subscriptionTypeID = subscriptionTypeID;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(int periodInMonths) {
        this.periodInMonths = periodInMonths;
    }
}
