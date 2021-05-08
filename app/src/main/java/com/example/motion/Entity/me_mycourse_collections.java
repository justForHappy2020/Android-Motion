package com.example.motion.Entity;

public class me_mycourse_collections {

    private String imgUrls;
    private String collectionsName1;
    private String collectionsName2;
    private String collectionsName3;


    public me_mycourse_collections(){

    }

    public me_mycourse_collections(String imgUrls, String collectionsName1, String collectionsName2, String collectionsName3){
        this.collectionsName1 = collectionsName1;
        this.collectionsName2 = collectionsName2;
        this.collectionsName3 = collectionsName3;
        this.imgUrls = imgUrls;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public String getCollectionsName1() {
        return collectionsName1;
    }

    public String getCollectionsName2() {
        return collectionsName2;
    }

    public String getCollectionsName3() {
        return collectionsName3;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setCollectionsName1(String collectionsName1) {
        this.collectionsName1 = collectionsName1;
    }

    public void setCollectionsName2(String collectionsName2) {
        this.collectionsName2 = collectionsName2;
    }

    public void setCollectionsName3(String collectionsName3) {
        this.collectionsName3 = collectionsName3;
    }
}
