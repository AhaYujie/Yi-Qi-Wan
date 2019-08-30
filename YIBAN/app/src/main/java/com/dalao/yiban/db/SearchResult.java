package com.dalao.yiban.db;

import java.util.List;

public class SearchResult {
    /**
     * pageviews : 1
     * time : 2019-08-06 13:31:38
     * title : 竞赛标题测试
     * author : 网站发布人
     * id : 6
     **/

    private int pageviews;
    private String time;
    private String title;
    private String author;
    private int id;
    private String userid;
    private int type;
    private String avater;

    public SearchResult(int pageviews,String time,String title,int id,String userid,int type,String avater)
    {
        this.pageviews=pageviews;
        this.time=time;
        this.title=title;
        this.id=id;
        this.userid = userid;
        this.type=type;
        this.avater=avater;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid =userid;
    }

    public int getType(){
        return type;
    }

    public void setType(){
        this.type=type;
    }

    public String getAvater(){
        return avater;
    }

    public void setAvater(String avater){
        this.avater=avater;
    }
}
