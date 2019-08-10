package com.dalao.yiban.util;

import com.dalao.yiban.db.Comment;
import com.dalao.yiban.gson.ActivityGson;
import com.dalao.yiban.gson.CommentBean;
import com.dalao.yiban.gson.ContestGson;
import com.dalao.yiban.gson.HomeListGson;
import com.dalao.yiban.gson.ReplyGson;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class JsonUtil {

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

}
