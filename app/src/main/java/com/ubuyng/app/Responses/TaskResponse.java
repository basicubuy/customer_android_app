/*
 * Copyright (c) 2021. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.Responses;

public class TaskResponse {

    private String project_id;
    private String sub_category_id;
    private String user_id;
    private String sub_category_name;
    private String project_title;
    private String address;
    private String brief;
    private String task_amount;
    private String task_status;
    private String selected_pro_image;
    private String pro_name;
    private String pro_id;
    private String bid_id;
    private String started_at;
    private String deadline_at;

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getTask_amount() {
        return task_amount;
    }

    public void setTask_amount(String task_amount) {
        this.task_amount = task_amount;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getSelected_pro_image() {
        return selected_pro_image;
    }

    public void setSelected_pro_image(String selected_pro_image) {
        this.selected_pro_image = selected_pro_image;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getBid_id() {
        return bid_id;
    }

    public void setBid_id(String bid_id) {
        this.bid_id = bid_id;
    }

    public String getStarted_at() {
        return started_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public String getDeadline_at() {
        return deadline_at;
    }

    public void setDeadline_at(String deadline_at) {
        this.deadline_at = deadline_at;
    }
}
