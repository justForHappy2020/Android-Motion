package com.example.motion.Entity;

public class me_mycollections_articals {

    private String imgUrls;
    private String articalName;
    private String articalAuthor;
    private String articalTime;

    public me_mycollections_articals(){

    }

    public me_mycollections_articals(String imgUrls, String articalsName, String articalsAuthor, String articalsTime){
        this.articalAuthor = articalsAuthor;
        this.articalName = articalsName;
        this.articalTime = articalsTime;
        this.imgUrls = imgUrls;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public String getArticalAuthor() {
        return articalAuthor;
    }

    public String getArticalName() {
        return articalName;
    }

    public String getArticalTime() {
        return articalTime;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setArticalAuthor(String articalAuthor) {
        this.articalAuthor = articalAuthor;
    }

    public void setArticalName(String articalName) {
        this.articalName = articalName;
    }

    public void setArticalTime(String articalTime) {
        this.articalTime = articalTime;
    }
}
