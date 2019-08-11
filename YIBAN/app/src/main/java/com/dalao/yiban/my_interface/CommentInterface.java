package com.dalao.yiban.my_interface;

import android.view.View;

public interface CommentInterface {

    /**
     * 编辑评论
     * @param toUserId:回复的用户id(若无则为-1)
     */
    void editCommentText(String toUserId);

    /**
     * 发表评论
     */
    void publishComment();

}
