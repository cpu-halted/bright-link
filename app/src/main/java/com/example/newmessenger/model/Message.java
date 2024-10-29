package com.example.newmessenger.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    final Long id;
    final String type;
    final String content;
    @SerializedName("sender_id")
    final Long senderId;
    final Long replies;

    public Message(Long id, String type, String content, Long senderId, Long replies) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.senderId = senderId;
        this.replies = replies;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return content;
    }

    public String getContent() {
        return content;
    }

    public Long getSenderId() {
        return senderId;
    }
}
