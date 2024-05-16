package com.example.homework.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subscript {
    @PrimaryKey(autoGenerate = true)
    public int subId;
    public int userId;
    public String url;

    public String name;
    public String message;

    public String content;
    public String username;
    public Subscript(int subId, int userId, String url, String content) {
        this.subId = subId;
        this.userId = userId;
        this.url = url;
        this.content = content;
    }
    public Subscript(int subId,String name,String message ,int userId, String url, String content,String username) {
        this.subId = subId;
        this.userId = userId;
        this.url = url;
        this.content = content;
        this.message = message;
        this.name = name;
        this.username = username;
    }
}