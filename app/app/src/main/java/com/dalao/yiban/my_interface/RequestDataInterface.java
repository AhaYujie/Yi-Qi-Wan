package com.dalao.yiban.my_interface;

public interface RequestDataInterface {

    /**
     * 竞赛，活动，博客，查看回复请求服务器获取数据刷新UI
     * @param updateContent : true则更新内容UI
     * @param updateComment : true则更新评论区UI
     */
    void requestDataFromServer(final boolean updateContent, final boolean updateComment);

}
