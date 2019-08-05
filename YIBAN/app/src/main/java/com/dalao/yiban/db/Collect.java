package com.dalao.yiban.db;

import org.litepal.crud.DataSupport;

import java.util.Date;


/**
 * 用户收藏
 */
public class Collect extends DataSupport {

    private int id;

    private int serverDbId;     // 服务器数据库id

    private int userId;         // 所属用户id(服务器数据库id)

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

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
