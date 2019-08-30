package com.dalao.yiban.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsedSearch {

    private int userid;
    private String content;
    private int searchid;

    public UsedSearch(String content,int userid,int searchid) {
        this.content=content;
        this.userid = userid;
        this.searchid =searchid;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content){
        this.content=content;
    }

    public int getUserId(){
        return userid;
    }

    public void setUserId(int id){
        this.userid=id;
    }

    public int getSearchid(){
        return searchid;
    }

    public void setSearchid(int searchid){
        this.searchid = searchid;
    }

}
