package com.example.newmessenger.model;

import com.google.gson.annotations.SerializedName;

public class Conversation
{
    final Long id;
    final String title;
    final String description;

    public Conversation(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
