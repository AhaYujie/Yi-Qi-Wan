package com.dalao.yiban.my_interface;

public interface CollectInterface {

    /**
     * 收藏成功
     */
    void collectSuccess();

    /**
     * 取消收藏成功
     */
    void unCollectSuccess();

    /**
     * 收藏或取消收藏失败
     */
    void collectError();

    /**
     * 进行收藏或取消收藏操作
     */
    void collectStart();

}
