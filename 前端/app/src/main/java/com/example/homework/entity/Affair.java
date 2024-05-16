package com.example.homework.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Affair {
    @PrimaryKey(autoGenerate = true)
    public int affairId;
    public int userId;
    public int type;// 1表示计时 2表示倒计时
    public String message;//表示事务的类型
    public int imageId;//图片
    public int time;

    public Affair(int affairId,int userId, int type, String message, int imageId, int time) {
        this.affairId = affairId;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.imageId = imageId;
        this.time = time;
    }
}
