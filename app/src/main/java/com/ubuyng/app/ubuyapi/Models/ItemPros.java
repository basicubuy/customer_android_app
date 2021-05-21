package com.ubuyng.app.ubuyapi.Models;
/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
public class ItemPros {

    private String proId;
    private String proName;
    private String projectCount;
    private String profileImage;
    private String premiumPro;
    private String proService;
    private String aboutPro;

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() { return proName; }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(String projectCount) {
        this.projectCount = projectCount;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPremiumPro() {
        return premiumPro;
    }

    public void setPremiumPro(String premiumPro) {
        this.premiumPro = premiumPro;
    }

    public String getProService() {
        return proService;
    }

    public void setProService(String proService) {
        this.proService = proService;
    }

    public String getAboutPro() { return aboutPro; }

    public void setAboutPro(String aboutPro) { this.aboutPro = aboutPro; }
}
