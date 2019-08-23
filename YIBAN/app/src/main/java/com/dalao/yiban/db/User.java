package com.dalao.yiban.db;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;


/**
 * 用户
 */
public class User extends DataSupport {

    private int id;

    private String severId;             // 服务器用户id

    private List<Search> searchList;    // 搜索历史列表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeverId() {
        return severId;
    }

    public void setSeverId(String severId) {
        this.severId = severId;
    }

    public List<Search> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<Search> searchList) {
        this.searchList = searchList;
    }
}
