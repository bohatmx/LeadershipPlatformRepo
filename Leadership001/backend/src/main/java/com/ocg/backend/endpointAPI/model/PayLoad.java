package com.ocg.backend.endpointAPI.model;

import com.googlecode.objectify.annotation.Entity;
import com.ocg.backend.endpointAPI.model.Data;

import java.io.Serializable;

/**
 * Created by nkululekochrisskosana on 2017/11/09.
 */

@Entity
public class PayLoad implements Serializable {
    String to;
    Data data;

    public PayLoad() {
    }

    public PayLoad(String to, Data data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
