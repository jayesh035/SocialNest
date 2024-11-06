package com.example.social_medial_app.Models;

public class Reel {
    public String videoUrl = "";
    public String caption = "";
    public String profileLink=null;

    Reel()
    {}

    public Reel(String videoUrl, String caption) {
        this.videoUrl = videoUrl;
        this.caption = caption;
    }

    public Reel(String videoUrl, String caption, String profileLink) {
        this.videoUrl = videoUrl;
        this.caption = caption;
        this.profileLink = profileLink;
    }
}
