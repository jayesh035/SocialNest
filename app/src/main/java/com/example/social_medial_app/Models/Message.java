package com.example.social_medial_app.Models;

public class Message {

    public String senderId=null;
    public String message_text=null;
    public long timestamp;

    public Message() {
    }

    public Message(String senderId, String message_text, long timestamp) {
        this.senderId = senderId;
        this.message_text = message_text;
        this.timestamp = timestamp;
    }


}
