package com.dalao.yiban.my_interface;

public interface CommentInterface {

    /**
     * 编辑评论
     * @param toCommentId:回复的评论的id(若无则为-1)
     */
    void editCommentText(String toCommentId);

    /**
     * 发表评论
     */
    void publishComment();

}
