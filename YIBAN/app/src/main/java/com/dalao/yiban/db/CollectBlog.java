package com.dalao.yiban.db;

import androidx.annotation.Nullable;

public class CollectBlog {

    private String title;
    private String time;
    private int pageviews;
    private int id;
    private int userid;

    public CollectBlog(String title,String time,int pageviews,int id,int userid){
        this.pageviews=pageviews;
        this.time=time;
        this.title=title;
        this.id= id;
        this.userid= userid;
    }

    public int getPageviews() {
        return pageviews;
    }

    public void setPageviews(int pageviews) {
        this.pageviews = pageviews;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId(){
        return id;
    }

    public void setId(){
        this.id = id;
    }

    public int getUserid(){
        return userid;
    }

    public void setUserid(){
        this.userid = userid;
    }
}
