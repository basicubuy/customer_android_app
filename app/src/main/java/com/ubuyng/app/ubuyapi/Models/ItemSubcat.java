package com.ubuyng.app.ubuyapi.Models;
/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
public class ItemSubcat {

    private String subId;
    private String catId;
    private String subTitle;
    private String subSecName;
    private String subPic;
    private String subIcon;
    private String subDes;



//feed id
    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

//    title
    
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

//    featured pic
    public String getSubPic() {
        return subPic;
    }

    public void setSubPic(String subPic) {
        this.subPic = subPic;
    }
//    cion pic
    public String getSubIcon() {
        return subIcon;
    }

    public void setSubIcon(String subIcon) {
        this.subIcon = subIcon;
    }

    //   description
    public String getSubDes() {
        return subDes;
    }

    public void setSubDes(String subDes) {
        this.subDes = subDes;
    }
    //   category id
    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    //   secondary name
    public String getSubSecName() {
        return subSecName;
    }

    public void setSubSecName(String subSecName) {
        this.subSecName = subSecName;
    }

}
