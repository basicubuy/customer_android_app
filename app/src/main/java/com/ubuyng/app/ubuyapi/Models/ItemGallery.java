package com.ubuyng.app.ubuyapi.Models;
/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
public class ItemGallery {

    private String gelleryId;
    private String galleryService;
    private String galleryTitle;
    private String galleryImg;


    public String getGalleryImg() {
        return galleryImg;
    }

    public void setGalleryImg(String galleryImg) {
        this.galleryImg = galleryImg;
    }

    public String getGalleryTitle() {
        return galleryTitle;
    }

    public void setGalleryTitle(String galleryTitle) {
        this.galleryTitle = galleryTitle;
    }

    public String getGalleryService() {
        return galleryService;
    }

    public void setGalleryService(String galleryService) {
        this.galleryService = galleryService;
    }

    public String getGelleryId() {
        return gelleryId;
    }

    public void setGelleryId(String gelleryId) {
        this.gelleryId = gelleryId;
    }
}
