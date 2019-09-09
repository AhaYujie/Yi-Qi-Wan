package com.dalao.yiban.my_interface;

public interface FollowInterface {

    /**
     * 关注成功
     * @param userId : 关注的用户id
     */
    void followSucceed(String userId);

    /**
     * 取消关注成功
     * @param userId : 关注的用户id
     */
    void unFollowSucceed(String userId);

    /**
     * 关注或取消关注成功
     * @param userId : 关注的用户id
     */
    void followError(String userId);

    /**
     * 进行关注或取消关注操作
     * @param userId : 关注的用户id
     */
    void followStart(String userId);

}
