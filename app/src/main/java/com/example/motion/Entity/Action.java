package com.example.motion.Entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = CourseCahceDatabase.class)
public class Action extends BaseModel implements Serializable {
    static final public int COUNTING = 1;
    static final public int TIMING = 2;

    @PrimaryKey()
    @Column
    private Long actionID;

    @Column
    private String actionName;

    @Column
    private String actionImgs;

    @Column
    private String actionUrl;

    @Column
    private String actionLocUrl;

    @Column
    private String intro;

    @Column
    private int duration;

    @Column
    private int type;

    @Column
    private int count;

    @Column
    private int total;

    @Column
    private int restDuration;

    @Column
    private int sizeByte;

    @ForeignKey(stubbedRelationship = true)
    private Course ownerCourse;


    public Action() {

    }

    public Action(Long actionID, String actionName, String actionImgs, String actionUrl, String intro, int duration, int type, int count, int total, int restDuration) {
        this.actionID = actionID;
        this.actionName = actionName;
        this.actionImgs = actionImgs;
        this.actionUrl = actionUrl;
        this.intro = intro;
        this.duration = duration;
        this.type = type;
        this.count = count;
        this.total = total;
        this.restDuration = restDuration;
    }

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionImgs() {
        return actionImgs;
    }

    public void setActionImgs(String actionImgs) {
        this.actionImgs = actionImgs;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getActionLocUrl() {
        return actionLocUrl;
    }

    public void setActionLocUrl(String actionLocUrl) {
        this.actionLocUrl = actionLocUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(int restDuration) {
        this.restDuration = restDuration;
    }

    public Course getOwnerCourse() {
        return ownerCourse;
    }

    public void setOwnerCourse(Course ownerCourse) {
        this.ownerCourse = ownerCourse;
    }

    public int getSizeByte() {
        return sizeByte;
    }

    public void setSizeByte(int sizeByte) {
        this.sizeByte = sizeByte;
    }
}
