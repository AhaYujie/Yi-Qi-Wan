package com.dalao.yiban.db;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;


/**
 * 用户
 */
public class User extends DataSupport {

    private int id;

    private int serverDbId;     // 服务器数据库id

    private String name;        // 用户名

    private String pwd;         // 用户密码

    private int sex;            // 性别

    private String face;        // 头像

    private Date createTime;    // 用户创建时间

    private List<Comment> comments;  // 用户评论

    private List<Collect> collects;  // 用户收藏

    private List<Follow>    follows; // 用户关注

    private List<Search> searches;   // 用户搜索历史

    private List<Chat> chats;

    private List<Blog> blogs;   // 用户博客

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
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

    public List<Collect> getCollects() {
        return collects;
    }

    public void setCollects(List<Collect> collects) {
        this.collects = collects;
    }

    public List<Search> getSearches() {
        return searches;
    }

    public void setSearches(List<Search> searches) {
        this.searches = searches;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public List<Follow> getFollows() {
        return follows;
    }

    public void setFollows(List<Follow> follows) {
        this.follows = follows;
    }
}
