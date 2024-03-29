package com.dalao.yiban.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 评论的json
 */
public class CommentBean implements Serializable {

    /**
     * content : 我现在在测试就像个傻逼一样，不想评论你一次。。。
     * number : 7
     * avatar : /static/photo/8.jpeg
     * author : lmp
     * time : 2019-08-09 23:19:04
     * id : 1
     * user_id: 1
     */

    private String content;
    private int number;
    private String avatar;
    private String author;
    private String time;
    private int id;
    @SerializedName("user_id")
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
