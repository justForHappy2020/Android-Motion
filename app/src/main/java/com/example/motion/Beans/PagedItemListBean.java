package com.example.motion.Beans;

import com.example.motion.Entity.Course;
import com.example.motion.Entity.MultipleItem;

import java.util.List;

public class PagedItemListBean {
    private List<MultipleItem> courseList;
    private boolean hasNext;

    public List<MultipleItem> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<MultipleItem> courseList) {
        this.courseList = courseList;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
