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

    public SearchResult(int pageviews,String time,String title,String author,int id)
    {
        this.pageviews=pageviews;
        this.time=time;
        this.title=title;
        this.author=author;
        this.id=id;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
