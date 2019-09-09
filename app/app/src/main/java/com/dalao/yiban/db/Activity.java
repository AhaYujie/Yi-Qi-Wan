package com.dalao.yiban.db;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

/**
 * 活动
 */
public class Activity extends DataSupport {

    private int id;

    private int serverDbId;     // 服务器数据库id

    private String title;       // 活动标题

    private String content;     // 活动内容

    private String author;      // 作者

    private int numOfView;      // 浏览次数

    private Date createTime;    // 添加时间

    private List<Comment> comments;   // 评论

    private List<Search> searches;    // 搜索历史

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

    public int getNumOfView() {
        return numOfView;
    }

    public void setNumOfView(int numOfView) {
        this.numOfView = numOfView;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Search> getSearches() {
        return searches;
    }

    public void setSearches(List<Search> searches) {
        this.searches = searches;
    }
}
