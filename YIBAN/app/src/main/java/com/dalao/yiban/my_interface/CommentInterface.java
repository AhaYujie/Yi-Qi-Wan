package com.dalao.yiban.my_interface;

import android.view.View;

public interface CommentInterface {

    /**
     * 编辑评论
     */
    void editCommentText();

    /**
     * 发表评论
     * @param toUserId:回复的用户id(若无则为-1)
     */
    void publishComment(String toUserId);

}
