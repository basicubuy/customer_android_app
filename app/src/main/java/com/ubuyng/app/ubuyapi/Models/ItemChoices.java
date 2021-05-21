package com.ubuyng.app.ubuyapi.Models;
/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
public class ItemChoices {

    private String choiceId;
    private String quesId;
    private String choiceText;
    private String choiceDesc;
    private String choiceType;
    private Boolean isSelected;

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public String getQuesId() {
        return quesId;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public String getChoiceDesc() {
        return choiceDesc;
    }

    public void setChoiceDesc(String choiceDesc) {
        this.choiceDesc = choiceDesc;
    }

    public String getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(String choiceType) {
        this.choiceType = choiceType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean b) {
    }
}
