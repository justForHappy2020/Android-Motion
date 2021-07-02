package com.example.motion.Entity;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ManyToMany;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.List;

@Table(database = CourseCahceDatabase.class)
public class Course extends BaseModel implements Serializable {
    @PrimaryKey()
    private Long courseId;

    @Column
    private String courseName;

    @Column
    private String backgroundUrl;

    @Column
    private String duration;

    @Column
    private int hit;//建议改为String

    @Column
    private String createTime;

    @Column
    private String courseIntro;

    @Column
    private String labels;

    @Column
    private String targetAge;

    @Column
    private Long collectionNumber;

    @Column
    private int isOnline;

    @Column
    private boolean collected;

    /*
    List<com.example.motion.Entity.Action> actionList;
    @OneToMany(methods = {OneToMany.Method.ALL},variableName = "actionList")
    public List<com.example.motion.Entity.Action> getActionList(){
        if (actionList == null || actionList.isEmpty()){
            //actionList = new Select().from(com.example.motion.Entity.Action.class).where(Action_Table.ownerCourse_courseId.eq(courseId)).queryList();
        }
        return actionList;
    }
    public void setActionList(List<com.example.motion.Entity.Action> actionList) {
        this.actionList = actionList;
    }
     */

    private List<com.example.motion.Entity.Action> actionList;

    public List<com.example.motion.Entity.Action> getActionList(){
        if (actionList == null || actionList.isEmpty()){
            actionList = new Select()
                    .from(com.example.motion.Entity.Action.class)
                    .where(Action_Table.actionID.in(
                            new Select(Course_Action_Table.action_actionID)
                                    .from(Course_Action.class)
                                    .where(Course_Action_Table.course_courseId.eq(courseId))
                            )
                    )
                    .queryList();
        }
        return actionList;
    }


    public void setActionList(List<com.example.motion.Entity.Action> actionList) {
        this.actionList = actionList;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCourseIntro() {
        return courseIntro;
    }

    public void setCourseIntro(String courseIntro) {
        this.courseIntro = courseIntro;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getTargetAge() {
        return targetAge;
    }

    public void setTargetAge(String targetAge) {
        this.targetAge = targetAge;
    }

    public Long getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(Long collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
