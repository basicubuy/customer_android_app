package com.ubuyng.app.ubuyapi.Models;
/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
public class ItemCat {

    private String catId;
    private String catTitle;
    private String catPic;
    private String catDes;
    private String catColor;

    //   category id
    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    //    title
    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

//    featured pic
    public String getCatPic() {
        return catPic;
    }

    public void setCatPic(String catPic) {
        this.catPic = catPic;
    }

    //   description
    public String getCatDes() {
        return catDes;
    }

    public void setCatDes(String catDes) {
        this.catDes = catDes;
    }

    public String getCatColor() {
        return catColor;
    }

    public void setCatColor(String catColor) {
        this.catColor = catColor;
    }
}
