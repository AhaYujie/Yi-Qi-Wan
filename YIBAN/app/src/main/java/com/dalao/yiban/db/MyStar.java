package com.dalao.yiban.db;

import androidx.annotation.Nullable;

public class MyStar {

    private String avater;
    private String author;
    private int authorid;

    public MyStar(String avater,String author,int authorid){
        this.author=author;
        this.authorid=authorid;
        this.avater=avater;
    }

    public String getAvater(){
        return avater;
    }

    public void setAvater(String avater){
        this.avater=avater;
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
