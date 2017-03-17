package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class PaymentDTO implements DTOEntity, Serializable, Comparable<PaymentDTO> {
    private String companyID, paymentID,
            userID, subscriberName, companyName, stringPaymentDate;
    private boolean active;
    private long paymentDate;
    private double amount;

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getStringPaymentDate() {
        return stringPaymentDate;
    }

    public void setStringPaymentDate(String stringPaymentDate) {
        this.stringPaymentDate = stringPaymentDate;
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

    @Override
    public int compareTo(@NonNull PaymentDTO d) {
        if (paymentDate > d.paymentDate) {
            return -1;
        }
        if (paymentDate < d.paymentDate) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getTitleForAdapter() {
        return subscriberName;
    }

    @Override
    public String getTopText() {
        return stringPaymentDate;
    }

    @Override
    public String getBottomTitle() {
        return df.format(amount);
    }

    @Override
    public String getBottomText() {
        return companyName;
    }

    static final DecimalFormat df = new DecimalFormat("###,###,###,###,##0.00");
}
