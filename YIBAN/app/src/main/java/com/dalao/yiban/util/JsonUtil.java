package com.dalao.yiban.util;

import android.util.Log;

import com.dalao.yiban.gson.ContestListGson;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonUtil {

    /**
     * 解析竞赛列表数据json
     * @param response : 返回的json数据body
     * @return : ContestListGson
     */
    public static ContestListGson handleContestListResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            ContestListGson contestListGson = new ContestListGson();
            List<ContestListGson.ContestListBean> contestListBeans = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String content = jsonObject.toString();
                contestListBeans.add(new Gson().fromJson(content, ContestListGson.ContestListBean.class));
            }
            contestListGson.setContestList(contestListBeans);
            return contestListGson;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
