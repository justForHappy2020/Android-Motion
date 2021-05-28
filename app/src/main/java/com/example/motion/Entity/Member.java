package com.example.motion.Entity;

import java.io.Serializable;

public class Member implements Serializable {
    private Long memberID;
    private String memberName;
    private String gender;
    private String headPortraitUrl;
    private String birth;

    public Member(Long memberID, String memberName, String gender, String headPortraitUrl, String birth) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.gender = gender;
        this.headPortraitUrl = headPortraitUrl;
        this.birth = birth;
    }

    public Long getMemberID() {
        return memberID;
    }

    public void setMemberID(Long memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
