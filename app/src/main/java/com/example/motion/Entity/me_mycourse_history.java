package com.example.motion.Entity;

public class me_mycourse_history {

    private String imgUrls;
    private String historyName1;
    private String historyName2;
    private String historyName3;
    private String historyTimes;
    private String labels;
    private Long courseId;
    private int isOnline;
    public me_mycourse_history(){

    }

    public me_mycourse_history(String imgUrls, String historyName1, String historyName2, String historyName3, String historyTimes){
        this.historyName1 = historyName1;
        this.historyName2 = historyName2;
        this.historyName3 = historyName3;
        this.historyTimes = historyTimes;
        this.imgUrls = imgUrls;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getLabels() {
        return labels;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public String getHistoryName1() {
        return historyName1;
    }

    public String getHistoryName2() {
        return historyName2;
    }

    public String getHistoryName3() {
        return historyName3;
    }

    public String getHistoryTimes() {
        return historyTimes;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setHistoryName1(String historyName1) {
        this.historyName1 = historyName1;
    }

    public void setHistoryName2(String historyName2) {
        this.historyName2 = historyName2;
    }

    public void setHistoryName3(String historyName3) {
        this.historyName3 = historyName3;
    }

    public void setHistoryTimes(String historyTimes) {
        this.historyTimes = historyTimes;
    }
}
