package com.oneconnect.leadership.library.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 3/28/17.
 */

public class ClientDTO implements Serializable {

    private String clientID, clientName, email, cellphone, stringDateRegistered;
    private long dateRegistered;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");
    private HashMap<String, SubscriptionDTO> subscriptions;

    public ClientDTO() {
        dateRegistered = new Date().getTime();
        stringDateRegistered = sdf.format(new Date());
    }

    public HashMap<String, SubscriptionDTO> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(HashMap<String, SubscriptionDTO> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}
