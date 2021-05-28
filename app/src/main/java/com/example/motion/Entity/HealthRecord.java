package com.example.motion.Entity;

import java.io.Serializable;

public class HealthRecord implements Serializable {
    private String createTime;
    private int weight;
    private int height;
    private int bmi;
    private String pictureURL;

    public HealthRecord(String createTime, int weight, int height, int bmi, String pictureURL) {
        this.createTime = createTime;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.pictureURL = pictureURL;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
