package com.dalao.yiban.util;

import com.dalao.yiban.gson.HomeListGson;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtil {

    /**
     * 解析竞赛列表数据json
     * @param responseText : 返回的json数据body
     * @return : HomeListGson
     */
    public static HomeListGson handleContestListResponse(String responseText) {
        try {
            JSONObject jsonObject = new JSONObject(responseText);
            String content = jsonObject.toString();
            return new Gson().fromJson(content, HomeListGson.class);

        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
