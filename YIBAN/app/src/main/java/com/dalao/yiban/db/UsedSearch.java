package com.dalao.yiban.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsedSearch {

    private int id;
    private String content;
    private int searchid;

    public UsedSearch(String content,int id,int searchid) {
        this.content=content;
        this.id=id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content){
        this.content=content;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

}
