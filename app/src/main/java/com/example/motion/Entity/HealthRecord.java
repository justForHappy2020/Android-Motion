package com.example.motion.Entity;

import java.io.Serializable;

public class HealthRecord implements Serializable {
    private String createTime;
    private float weight;
    private float height;
    private float bmi;
    private int status;

    public HealthRecord() {
    }

    public HealthRecord(String createTime, float weight, float height, float bmi, int status) {
        this.createTime = createTime;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
