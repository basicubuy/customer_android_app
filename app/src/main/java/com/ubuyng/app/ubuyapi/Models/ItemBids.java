package com.ubuyng.app.ubuyapi.Models;
/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
public class ItemBids {

    private String projectId;
    private String bidId;
    private String bidderId;
    private String projectTitle;
    private String bidMessage;
    private String bidAmount;
    private String bidderImage;
    private String bidderName;
    private String bidDate;
    private String bidStatus;
    private String bidVersion;
    private String bidType;
    private String bidMaterialFee;
    private String bidServiceFee;

    public String getBidMaterialFee() {
        return bidMaterialFee;
    }

    public void setBidMaterialFee(String bidMaterialFee) {
        this.bidMaterialFee = bidMaterialFee;
    }

    public String getBidServiceFee() {
        return bidServiceFee;
    }

    public void setBidServiceFee(String bidServiceFee) {
        this.bidServiceFee = bidServiceFee;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getBidMessage() {
        return bidMessage;
    }

    public void setBidMessage(String bidMessage) {
        this.bidMessage = bidMessage;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getBidderImage() {
        return bidderImage;
    }

    public void setBidderImage(String bidderImage) {
        this.bidderImage = bidderImage;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getBidVersion() {
        return bidVersion;
    }

    public void setBidVersion(String bidVersion) {
        this.bidVersion = bidVersion;
    }
}
