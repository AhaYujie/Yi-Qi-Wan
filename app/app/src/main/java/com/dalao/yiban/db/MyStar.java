package com.dalao.yiban.db;

import androidx.annotation.Nullable;

public class MyStar {

    private String avatar;
    private String author;
    private int authorid;
    private int id;

    public MyStar(String avatar,String author,int authorid,int id){
        this.author=author;
        this.authorid=authorid;
        this.avatar=avatar;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar=avatar;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author=author;
    }

    public void setAuthorid(int authorid){
        this.authorid=authorid;
    }

    public int getAuthorid(){
        return authorid;
    }
}
