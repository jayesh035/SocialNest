package com.example.social_medial_app.Models;

public class HomePost {

    public  String postUrl;
    public  String profileImage;
    public String caption;
    public  String profileName;
    public Boolean isLiked;
    public String uploadTime;
   public HomePost()
    {}

    public HomePost(String postUrl, String profileImage, String caption, String profileName,String uploadTime) {
        this.postUrl = postUrl;
        this.profileImage = profileImage;
        this.caption = caption;
        this.profileName = profileName;
        this.isLiked=false;
        this.uploadTime=uploadTime;
    }
    public void setLiked(Boolean liked)
    {
        this.isLiked=liked;
    }
    public void setUploadTime(String time)
    {
        this.uploadTime=time;
    }


}
