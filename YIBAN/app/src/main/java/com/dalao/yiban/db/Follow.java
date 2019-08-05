package com.dalao.yiban.db;

import org.litepal.crud.DataSupport;

/**
 * 用户关注
 */
public class Follow extends DataSupport {

    private int id;

    private int serverDbId;     // 服务器数据库id

    private int userSrcId;      // 用户id(服务器数据库id)

    private int userDesId;      // 被关注用户id(服务器数据库id)


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

    public int getUserSrcId() {
        return userSrcId;
    }

    public void setUserSrcId(int userSrcId) {
        this.userSrcId = userSrcId;
    }

    public int getUserDesId() {
        return userDesId;
    }

    public void setUserDesId(int userDesId) {
        this.userDesId = userDesId;
    }
}
