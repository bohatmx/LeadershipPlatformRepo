package com.ocg.backend.endpointAPI.model;

/**
 * Created by nkululekochrisskosana on 2017/11/09.
 */

public class FCMResponseDTO {
    String message;
    int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
