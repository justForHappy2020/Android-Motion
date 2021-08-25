package com.example.motion.Entity;

public class sportMainItem {
    private Long courseId;
    private String courseName;
    private String TargetAge;
    private String Lables;
    private String imgUrl;
    public sportMainItem(){

    }
    public sportMainItem(String imgUrl,String courseName,String TargetAge,String Lables ){
        this.courseName = courseName;
        this.imgUrl = imgUrl;
        this.Lables = Lables;
        this.TargetAge = TargetAge;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTargetAge(String targetAge) {
        TargetAge = targetAge;
    }

    public void setLables(String lables) {
        Lables = lables;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTargetAge() {
        return TargetAge;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getLables() {
        return Lables;
    }
}
