package com.ubuyng.app.HelperClasses;


public class SectionorRow {
    private String row;
    private int circleImageView;
    private String desc;

    private String section;
    private boolean isRow;

    public static SectionorRow createRow(String row, String desc, int circleImageView) {
        SectionorRow sectionorRow = new SectionorRow();
        sectionorRow.row = row;
        sectionorRow.desc = desc;
        sectionorRow.circleImageView = circleImageView;
        sectionorRow.isRow = true;
        return sectionorRow;
    }

    public static SectionorRow createSection(String section) {
        SectionorRow sectionorRow = new SectionorRow();
        sectionorRow.section = section;
        sectionorRow.isRow = false;
        return sectionorRow;
    }

    public String getRow() {
        return row;
    }

    public String getSection() {
        return section;
    }

    public boolean isRow() {
        return isRow;
    }

    public int getCircleImageView() {
        return circleImageView;
    }

    public String getDesc() {
        return desc;
    }
}
