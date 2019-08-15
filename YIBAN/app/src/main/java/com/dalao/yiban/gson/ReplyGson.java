package com.dalao.yiban.gson;

import java.util.List;

/**
 * 查看回复的json
 */
public class ReplyGson {

    private List<CommentBean> comments;

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }
}
