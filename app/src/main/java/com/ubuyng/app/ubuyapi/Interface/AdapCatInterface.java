package com.ubuyng.app.ubuyapi.Interface;

public interface AdapCatInterface {
    void onCatClick(String catData, String catID);
    void onStateClick(String StateData, String StateID);
    void onSubCatClick(String subCatData, String subCatID, String subCatType);
    void onSubSkillClick(String skillData, String subCatID, String skillId);
}
