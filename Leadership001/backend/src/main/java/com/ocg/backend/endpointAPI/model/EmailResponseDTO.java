package com.ocg.backend.endpointAPI.model;

/**
 * Created by nkululekochrisskosana on 2017/11/09.
 */

public class EmailResponseDTO {
    int statusCode;
    String message;

    public EmailResponseDTO() {
    }

    public EmailResponseDTO(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
