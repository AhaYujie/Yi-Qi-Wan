package com.dalao.yiban.db;

import org.litepal.crud.DataSupport;


/**
 * 搜索历史
 */
public class Search extends DataSupport {

    private int id;

    private String keyWord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
