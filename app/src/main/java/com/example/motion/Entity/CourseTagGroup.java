package com.example.motion.Entity;

import java.io.Serializable;
import java.util.List;

public class CourseTagGroup implements Serializable {
    /**
     * 课程是否上线父标签id
     */
    public static final int TAG_GROUP_IS_ONLINE  = -1;

    /**
     * 课程排序父标签id
     */
    public static final int TAG_GROUP_SORT  = -2;

    private int groupId;
    private String groupName;
    private List<CourseTag> courseTagList;

    public CourseTagGroup() {
    }

    public CourseTagGroup(int groupId, String groupName, List<CourseTag> courseTagList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.courseTagList = courseTagList;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<CourseTag> getCourseTagList() {
        return courseTagList;
    }

    public void setCourseTagList(List<CourseTag> courseTagList) {
        this.courseTagList = courseTagList;
    }
}
