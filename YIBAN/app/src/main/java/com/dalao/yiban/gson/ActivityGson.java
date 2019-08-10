package com.dalao.yiban.gson;

import java.util.List;

public class ActivityGson {


    /**
     * content : 活动内容内容内容<img src="http://188888888.xyz:5000/static/photo/12.png"/>　活动内容内容内容　<img src="http://188888888.xyz:5000/static/photo/123.jpg"/>活动内容内容内容　
     * title : 活动我是活动1
     * collection : 1
     * pageviews : 12
     * comments : [{"content":"我现在在测试就像个傻逼一样，不想评论你一次。。。","number":7,"avatar":"/static/photo/8.jpeg","author":"lmp","time":"2019-08-09 23:19:04","id":1},{"content":"我现在在测试就像个傻逼一样，不想评论你两次。。。","number":2,"avatar":"/static/photo/8.jpeg","author":"lmp","time":"2019-08-09 23:19:04","id":2},{"content":"我现在在测试就像个傻逼一样，不想评论你,我是用户２，这是活动１。。。","number":1,"avatar":"/static/photo/123.jpg","author":"lzk","time":"2019-08-09 23:19:04","id":3},{"content":"我现在在测试就像个傻逼一样，不想评论你，我是用户３。这是活动１。。","number":1,"avatar":"/static/photo/12.png","author":"abc","time":"2019-08-09 23:19:04","id":4},{"content":"我现在在测试就像个傻逼一样，不想评论你，我是用户４。。这是活动１。","number":1,"avatar":"/static/photo/12.png","author":"lhb","time":"2019-08-09 23:19:04","id":5}]
     * author : lmp
     * time : 2019-08-09 23:19:03
     */

    private String content;
    private String title;
    private int collection;
    private int pageviews;
    private String author;
    private String time;
    private List<CommentBean> comments;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getPageviews() {
        return pageviews;
    }

    public void setPageviews(int pageviews) {
        this.pageviews = pageviews;
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

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }
}
