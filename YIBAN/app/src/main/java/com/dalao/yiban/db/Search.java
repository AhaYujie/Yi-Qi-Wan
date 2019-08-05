package com.dalao.yiban.db;

import org.litepal.crud.DataSupport;


/**
 * 搜索历史
 */
public class Search extends DataSupport {

    private int id;

    private int serverDbId;     // 服务器数据库id

    private String keyWord;     // 搜索关键字

    private int userId;         // 所属用户id(服务器数据库id)

    private int contestId;      // 所属竞赛id(服务器数据库id)

    private int activityId;     // 所属活动id(服务器数据库id)

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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
}
