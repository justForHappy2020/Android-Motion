package com.example.motion.Entity;

import java.io.Serializable;

public class Action implements Serializable {
    static final public int COUNTING = 1;
    static final public int TIMING = 2;

    private Long actionID;
    private String actionName;
    private String actionImgs;
    private String actionUrl;
    private String duration;
    private String intro;
    private int time;
    private int type;

    public Action() {

    }

    public Action(Long ActionID, String actionName, String actionImgs, String actionUrl, String duration, Long introId, String intro) {
        this.actionID = ActionID;
        this.actionName = actionName;
        this.actionImgs = actionImgs;
        this.actionUrl = actionUrl;
        this.duration = duration;
        this.actionID = introId;
        this.intro = intro;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
