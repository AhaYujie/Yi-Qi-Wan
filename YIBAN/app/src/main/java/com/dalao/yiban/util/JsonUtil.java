package com.dalao.yiban.util;

import com.dalao.yiban.gson.ActivityGson;
import com.dalao.yiban.gson.BlogGson;
import com.dalao.yiban.gson.CommentStatusGson;
import com.dalao.yiban.gson.CommentsGson;
import com.dalao.yiban.gson.CommunityBlogListGson;
import com.dalao.yiban.gson.CollectGson;
import com.dalao.yiban.gson.ContestGson;
import com.dalao.yiban.gson.CreateBlogGson;
import com.dalao.yiban.gson.EditUserInfoGson;
import com.dalao.yiban.gson.FollowGson;
import com.dalao.yiban.gson.HomeListGson;
import com.dalao.yiban.gson.MyBLogGson;
import com.dalao.yiban.gson.PostImageGson;
import com.dalao.yiban.gson.ReplyGson;
import com.dalao.yiban.gson.UserInfoGson;
import com.google.gson.Gson;


public class JsonUtil {

    /**
     * 解析我的博客数据json
     * @param responseText : 返回的json数据body
     * @return : MyBLogGson
     */
    public static MyBLogGson handleMyBlogResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, MyBLogGson.class);

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析评论区数据json
     * @param responseText : 返回的json数据body
     * @return : CommentsGson
     */
    public static CommentsGson handleCommentsResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, CommentsGson.class);

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析home列表数据json
     * @param responseText : 返回的json数据body
     * @return : 解析后的HomeListGson
     */
    public static HomeListGson handleHomeListResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, HomeListGson.class);

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析竞赛内容数据json
     * @param responseText：返回的json数据body
     * @return : 解析后的ContestGson
     */
    public static ContestGson handleContestResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, ContestGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析活动内容数据json
     * @param responseText：返回的json数据body
     * @return : ActivityGson
     */
    public static ActivityGson handleActivityResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, ActivityGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析评论数据json
     * @param responseText：返回的json数据body
     * @return : ActivityGson
     */
    public static ReplyGson handleReplyResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, ReplyGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析博客列表数据json
     * @param responseText：返回的json数据body
     * @return : CommunityBlogListGson
     */
    public static CommunityBlogListGson handleCommunityBlogListResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, CommunityBlogListGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析博客列表数据json
     * @param responseText：返回的json数据body
     * @return : CommunityBlogListGson
     */
    public static BlogGson handleBlogResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, BlogGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析收藏和取消收藏数据json
     * @param responseText：返回的json数据body
     * @return : CommunityBlogListGson
     */
    public static CollectGson handleContestCollectResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, CollectGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析用户信息数据json
     * @param responseText：返回的json数据body
     * @return : UserInfoGson
     */
    public static UserInfoGson handleUserResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, UserInfoGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析评论返回的json
     * @param responseText：返回的json数据body
     * @return : CommentStatusGson
     */
    public static CommentStatusGson handleCommentResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, CommentStatusGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析关注返回的json
     * @param responseText：返回的json数据body
     * @return : FollowGson
     */
    public static FollowGson handleFollowResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, FollowGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析关注返回的json
     * @param responseText：返回的json数据body
     * @return : EditUserInfoGson
     */
    public static EditUserInfoGson handleEditUserInfoResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, EditUserInfoGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析关注返回的json
     * @param responseText：返回的json数据body
     * @return : CreateBlogGson
     */
    public static CreateBlogGson handleCreateBlogResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, CreateBlogGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  解析传输图片的json
     * @param responseText：返回的json数据body
     * @return : PostImageGson
     */
    public static PostImageGson handlePostImageResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, PostImageGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
