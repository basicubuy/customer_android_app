package com.ubuyng.app.ubuyapi.Models;
/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
public class ItemBadges {

    private String badgeProId;
    private String badgeEmail;
    private String badgePhone;
    private String badgeID;

    public String getBadgeID() {
        return badgeID;
    }

    public void setBadgeID(String badgeID) {
        this.badgeID = badgeID;
    }

    public String getBadgePhone() {
        return badgePhone;
    }

    public void setBadgePhone(String badgePhone) {
        this.badgePhone = badgePhone;
    }

    public String getBadgeEmail() {
        return badgeEmail;
    }

    public void setBadgeEmail(String badgeEmail) {
        this.badgeEmail = badgeEmail;
    }

    public String getBadgeProId() {
        return badgeProId;
    }

    public void setBadgeProId(String badgeProId) {
        this.badgeProId = badgeProId;
    }
}
