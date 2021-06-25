package com.example.motion.Entity;

import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.example.motion.Entity.Action;
import com.example.motion.Entity.Course;

@Table(database = CourseCahceDatabase.class)
public class Course_Action extends BaseModel{
    @PrimaryKey(
            autoincrement = true
    )
    long _id;

    @ForeignKey(
            saveForeignKeyModel = false
    )
    com.example.motion.Entity.Action action;

    @ForeignKey(
            saveForeignKeyModel = false
    )
    com.example.motion.Entity.Course course;

    public Course_Action() {
    }

    public Course_Action(com.example.motion.Entity.Course course, com.example.motion.Entity.Action action) {
        this.action = action;
        this.course = course;
    }

    public final long getId() {
        return _id;
    }

    public final com.example.motion.Entity.Action getAction() {
        return action;
    }

    public final void setAction(com.example.motion.Entity.Action param) {
        action = param;
    }

    public final com.example.motion.Entity.Course getCourse() {
        return course;
    }

    public final void setCourse(com.example.motion.Entity.Course param) {
        course = param;
    }
}
