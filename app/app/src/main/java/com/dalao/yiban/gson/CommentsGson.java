package com.dalao.yiban.gson;

import java.util.List;

/**
 * 评论区Gson
 */
public class CommentsGson {

    private List<CommentBean> comments;

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }
}
