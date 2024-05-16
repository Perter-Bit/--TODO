package com.example.homework.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Detail {
    @PrimaryKey(autoGenerate = true)
    public int detailId;
    public int affairId;
    public long date;
    public long time;//秒为单位
    public boolean is_over;

    public Detail(int affairId, long date, long time,boolean is_over) {
        this.affairId = affairId;
        this.date = date;
        this.time = time;
        this.is_over = is_over;
    }
}
