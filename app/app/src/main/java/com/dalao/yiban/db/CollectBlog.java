package com.dalao.yiban.db;

import androidx.annotation.Nullable;

public class CollectBlog {

    private String title;
    private String time;
    private int pageviews;
    private int id;
    private int userid;
    private String avatar;
    private String name;
    private String authorid;

    public CollectBlog(String title,String time,int pageviews,int id,int userid,String avatar,String name,String authorid){
        this.pageviews=pageviews;
        this.time=time;
        this.title=title;
        this.id= id;
        this.userid= userid;
        this.avatar=avatar;
        this.name = name;
        this.authorid = authorid;
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

    public void setAvatar(String avatar){
        this.avatar= avatar;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setAuthorid(String authorid){
        this.authorid = authorid;
    }

    public String getAuthorid() {
        return authorid;
    }
}
