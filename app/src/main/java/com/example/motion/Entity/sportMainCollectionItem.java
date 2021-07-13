package com.example.motion.Entity;

public class sportMainCollectionItem {
    private String collectionCourseName;
    private String collectionTargetAge;
    private String collectionLables;
    private String collectionimgUrl;
    public sportMainCollectionItem(){

    }
    public sportMainCollectionItem(String imgUrl,String courseName,String TargetAge,String Lables ){
        this.collectionCourseName = courseName;
        this.collectionimgUrl = imgUrl;
        this.collectionLables = Lables;
        this.collectionTargetAge = TargetAge;
    }

    public String getCollectionCourseName() {
        return collectionCourseName;
    }

    public String getCollectionimgUrl() {
        return collectionimgUrl;
    }

    public String getCollectionLables() {
        return collectionLables;
    }

    public String getCollectionTargetAge() {
        return collectionTargetAge;
    }

    public void setCollectionCourseName(String collectionCourseName) {
        this.collectionCourseName = collectionCourseName;
    }

    public void setCollectionimgUrl(String collectionimgUrl) {
        this.collectionimgUrl = collectionimgUrl;
    }

    public void setCollectionLables(String collectionLables) {
        this.collectionLables = collectionLables;
    }

    public void setCollectionTargetAge(String collectionTargetAge) {
        this.collectionTargetAge = collectionTargetAge;
    }

}
