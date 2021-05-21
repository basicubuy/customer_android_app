package com.ubuyng.app.Chat;

/**
 * Created by Wahyu on 06/08/2015.
 */
public class ChatterModel {

    private String id;
    private String message;
    private String sender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
