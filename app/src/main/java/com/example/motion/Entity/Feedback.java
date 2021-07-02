package com.example.motion.Entity;

import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

public class Feedback implements Serializable {
    private Long feedbackId;
    private String content;
    private String picUrls;
    private String createTime;

    public Feedback(Long feedbackId, String content, String picUrls, String createTime) {
        this.feedbackId = feedbackId;
        this.content = content;
        this.picUrls = picUrls;
        this.createTime = createTime;
    }

    public Feedback() {

    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(String picUrls) {
        this.picUrls = picUrls;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
