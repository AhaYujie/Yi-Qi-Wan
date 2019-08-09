package com.dalao.yiban.util;

import com.dalao.yiban.gson.ContestGson;
import com.dalao.yiban.gson.HomeListGson;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtil {

    /**
     * 解析home列表数据json
     * @param responseText : 返回的json数据body
     * @return : HomeListGson
     */
    public static HomeListGson handleHomeListResponse(String responseText) {
        try {
//            JSONObject jsonObject = new JSONObject(responseText);
//            String content = jsonObject.toString();
            return new Gson().fromJson(responseText, HomeListGson.class);

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ContestGson handleContestResponse(String responseText) {
        try {
            return new Gson().fromJson(responseText, ContestGson.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
