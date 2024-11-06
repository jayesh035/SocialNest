package com.example.social_medial_app.Models;

import com.google.firebase.Timestamp;

public class Post {
    public String postUrl = "";
    public String caption = "";
    public String uid=null;
    public String timeStamp=null;
    Post()
    {}

    public Post(String postUrl, String caption) {
        this.postUrl = postUrl;
        this.caption = caption;
    }

    public Post(String postUrl, String caption, String uid, String timeStamp) {
        this.postUrl = postUrl;
        this.caption = caption;
        this.uid = uid;
        this.timeStamp = timeStamp;
    }


}
