package com.example.homework.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User
{
    @PrimaryKey(autoGenerate = true)
    public int userId;
    public String username;
    public String password;
    public String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
