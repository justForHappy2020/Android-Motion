package com.example.motion.Entity;

public class me_mycourse_reserve {
    private String imgUrls;
    private String reserveName1;
    private String reserveName2;
    private String reserveName3;
    private String reserveTime;
    private String labels;
    private Long courseId;
    private int isOnline;

    public me_mycourse_reserve(){

    }

    public me_mycourse_reserve(String imgUrls, String reserveName1, String reserveName2, String reserveName3, String reserveTime){
        this.imgUrls = imgUrls;
        this.reserveName1 = reserveName1;
        this.reserveName2 = reserveName2;
        this.reserveName3 = reserveName3;
        this.reserveTime = reserveTime;
    }

    public String getLabels() {
        return labels;
    }

    public Long getCourseId() {
        return courseId;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public String getReserveName1() {
        return reserveName1;
    }

    public String getReserveName2() {
        return reserveName2;
    }

    public String getReserveName3() {
        return reserveName3;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setReserveName1(String reserveName1) {
        this.reserveName1 = reserveName1;
    }

    public void setReserveName2(String reserveName2) {
        this.reserveName2 = reserveName2;
    }

    public void setReserveName3(String reserveName3) {
        this.reserveName3 = reserveName3;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }
}
