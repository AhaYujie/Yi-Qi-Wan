package com.dalao.yiban.db;

import org.litepal.crud.DataSupport;

import java.util.Date;


/**
 * 评论
 */
public class Comment extends DataSupport {

    private int id;

    private int serverDbId;     // 服务器数据库id

    private int userId;         // 所属用户id(服务器数据库id)

    private int activityId;     // 所属活动id(服务器数据库id)

    private int blogId;         // 所属博客id(服务器数据库id)

    private String comment;     // 评论内容

    private Date addTime;       // 添加时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerDbId() {
        return serverDbId;
    }

    public void setServerDbId(int serverDbId) {
        this.serverDbId = serverDbId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
