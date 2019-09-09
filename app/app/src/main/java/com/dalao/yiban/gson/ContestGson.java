package com.dalao.yiban.gson;

import java.util.List;

/**
 * 竞赛内容的json
 */
public class ContestGson {

    /**
     * type : h5
     * collection : 1
     * title : 竞赛标题测试１
     * content : 竞赛内容测试１
     * author : 网站发布人1
     * time : 2019-08-08 22:29:11
     * pageviews : 0
     */

    private String type;
    private int collection;
    private String title;
    private String content;
    private String author;
    private String time;
    private int pageviews;
    private List<CommentBean> comments;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPageviews() {
        return pageviews;
    }

    public void setPageviews(int pageviews) {
        this.pageviews = pageviews;
    }

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }

}
