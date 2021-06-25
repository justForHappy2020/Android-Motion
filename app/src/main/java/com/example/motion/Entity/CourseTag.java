package com.example.motion.Entity;

public class CourseTag {
    public static final int TAG_ONLINE_YES  = 1;
    public static final int TAG_ONLINE_NO  = 0;
    public static final int TAG_ONLINE_RESERVED  = 2;
    public static final int TAG_SORT_HOT  = 1;
    public static final int TAG_SORT_DATE  = 0;


    private int tagId;
    private String tagName;
    private String tagUrl;

    public CourseTag() {

    }

    public CourseTag(int tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl;
    }
}
