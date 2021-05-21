
/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.ubuyng.app.ubuyapi.Models;
public class ItemDispute {

    private String disputeId;
    private String disputeCat;
    private String TaskName;
    private String TaskRef;
    private String DisputeDate;
    private String DisputeStatus;



    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getTaskRef() {
        return TaskRef;
    }

    public void setTaskRef(String taskRef) {
        TaskRef = taskRef;
    }

    public String getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(String disputeId) {
        this.disputeId = disputeId;
    }

    public String getDisputeCat() {
        return disputeCat;
    }

    public void setDisputeCat(String disputeCat) {
        this.disputeCat = disputeCat;
    }

    public String getDisputeDate() {
        return DisputeDate;
    }

    public void setDisputeDate(String disputeDate) {
        DisputeDate = disputeDate;
    }

    public String getDisputeStatus() {
        return DisputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        DisputeStatus = disputeStatus;
    }
}
