package com.ubuyng.app.ubuyapi.Models;
/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
public class ItemPortfolio {

    private String portId;
    private String portLikes;
    private String portComments;
    private String portImg;
    private String portTitle;

    public String getPortLikes() {
        return portLikes;
    }

    public void setPortLikes(String portLikes) {
        this.portLikes = portLikes;
    }

    public String getPortComments() {
        return portComments;
    }

    public void setPortComments(String portComments) {
        this.portComments = portComments;
    }

    public String getPortTitle() { return portTitle; }

    public void setPortTitle(String portTitle) {
        this.portTitle = portTitle;
    }

    public String getPortImg() {
        return portImg;
    }

    public void setPortImg(String portImg) {
        this.portImg = portImg;
    }


    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }
}
