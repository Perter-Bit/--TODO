package com.example.homework.entity;

import androidx.room.PrimaryKey;

public class News
{
    @PrimaryKey(autoGenerate = true)
    public int newsId;
    public int userId;
    public String url;
    public String describe;//表示事务的类型
    public String title;
    public String img_url;
    public News(int newsId, int userId, String url, String describe, String title,String img_url) {
        this.newsId = newsId;
        this.userId = userId;
        this.url = url;
        this.describe = describe;
        this.title = title;
    }
}
