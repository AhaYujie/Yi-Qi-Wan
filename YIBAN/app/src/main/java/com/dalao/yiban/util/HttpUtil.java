package com.dalao.yiban.util;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dalao.yiban.MyApplication;
import com.dalao.yiban.constant.CommentConstant;
import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.CollectGson;
import com.dalao.yiban.gson.CommentGson;
import com.dalao.yiban.gson.EditUserInfoGson;
import com.dalao.yiban.gson.FollowGson;
import com.dalao.yiban.my_interface.CollectInterface;
import com.dalao.yiban.my_interface.CommentInterface;
import com.dalao.yiban.my_interface.FollowInterface;
import com.dalao.yiban.my_interface.RequestDataInterface;
import com.dalao.yiban.ui.activity.BaseActivity;
import com.dalao.yiban.ui.activity.BlogActivity;
import com.dalao.yiban.ui.activity.MainActivity;
import com.dalao.yiban.ui.activity.ViewFollowingActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 处理http请求的工具类
 */
public class HttpUtil {

    /**
     * GET请求
     * @param url:请求地址
     * @param callback:
     */
    public static void sendHttpGet(String url, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * POST请求
     * @param url:请求地址
     * @param formBody:提交的form格式数据
     * @param callback:
     */
    public static void sendHttpPost(String url, FormBody formBody, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 传输文件POST请求
     * @param url:请求地址
     * @param builder:RequestBody的builder
     * @param callback:
     */
    public static void sendHttpPostFile(String url, MultipartBody.Builder builder,
                                        okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 收藏或取消收藏
     * @param activity : 调用这个函数的活动
     * @param categorySelected: SELECT_CONTEST or SELECT_ACTIVITY or SELECT_BLOG
     * @param userId: 用户id
     * @param contentId: 文章id
     * @param isCollect : COLLECT or UN_COLLECT
     * @param collectInterface : 收藏接口
     */
    public static void collectContent(final BaseActivity activity, final int categorySelected,
                                      String userId, String contentId, int isCollect,
                                      CollectInterface collectInterface) {
        String category;
        String url;
        switch (categorySelected) {
            // 竞赛
            case HomeConstant.SELECT_CONTEST:
                category = ServerPostDataConstant.CONTEST_ID;
                if (isCollect == HomeConstant.UN_COLLECT) {
                    // 收藏
                    url = ServerUrlConstant.CONTEST_COLLECT_URI;
                } else {
                    // 取消收藏
                    url = ServerUrlConstant.CONTEST_UN_COLLECT_URI;
                }
                break;
            // 活动
            case HomeConstant.SELECT_ACTIVITY:
                category = ServerPostDataConstant.ACTIVITY_ID;
                if (isCollect == HomeConstant.UN_COLLECT) {
                    // 收藏
                    url = ServerUrlConstant.ACTIVITY_COLLECT_URI;
                } else {
                    // 取消收藏
                    url = ServerUrlConstant.ACTIVITY_UN_COLLECT_URI;
                }
                break;
            // 博客
            case HomeConstant.SELECT_BLOG:
                category = ServerPostDataConstant.BLOG_ID;
                if (isCollect == HomeConstant.UN_COLLECT) {
                    // 收藏
                    url = ServerUrlConstant.BLOG_COLLECT_URI;
                } else {
                    // 取消收藏
                    url = ServerUrlConstant.BLOG_UN_COLLECT_URI;
                }
                break;
            default:
                Toast.makeText(MyApplication.getContext(), HintConstant.COLLECT_ERROR,
                        Toast.LENGTH_SHORT).show();
                return;
        }

        FormBody formBody  = new FormBody.Builder()
                .add(category, contentId)
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();

        HttpUtil.sendHttpPost(url, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), HintConstant.COLLECT_ERROR,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                activity.getCallList().add(call);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    activity.getCallList().add(call);
                    String responseText = response.body().string();
                    final CollectGson collectGson = JsonUtil.handleContestCollectResponse(responseText);
                    if (collectGson.getMsg().equals(HomeConstant.CONTEST_COLLECT_SUCCESS_RESPONSE)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isCollect == HomeConstant.UN_COLLECT) {
                                    // 收藏
                                    collectInterface.collectSuccess();
                                } else {
                                    // 取消收藏
                                    collectInterface.unCollectSuccess();
                                }
                            }
                        });
                    } else if (collectGson.getMsg().equals(HomeConstant.CONTEST_COLLECT_ERROR_RESPONSE)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.getContext(), HintConstant.BLOG_AUTHOR_FOLLOW_ERROR,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), HintConstant.BLOG_AUTHOR_FOLLOW_ERROR,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 关注或取消关注博客作者
     * @param blogActivity: 此为null, 则viewFollowingActivity不为null
     * @param viewFollowingActivity: 此为null, 则blogActivity不为null
     * @param followInterface：
     * @param userId：用户id
     * @param authorId：博客作者id
     * @param activityType：BLOG_ACTIVITY or VIEW_FOLLOWING_ACTIVITY
     * @param followType：FOLLOW or UN_FOLLOW
     */
    public static void followBlogAuthor(final BlogActivity blogActivity, final ViewFollowingActivity viewFollowingActivity,
                                 final FollowInterface followInterface, String userId,
                                 String authorId, int activityType, final int followType) {
        final BaseActivity activity;
        switch (activityType) {
            case HomeConstant.BLOG_ACTIVITY:
                activity = blogActivity;
                break;
            case HomeConstant.VIEW_FOLLOWING_ACTIVITY:
                activity = viewFollowingActivity;
                break;
            default:
                Toast.makeText(MyApplication.getContext(), HintConstant.BLOG_AUTHOR_FOLLOW_ERROR,
                        Toast.LENGTH_SHORT).show();
                return;
        }
        String url;
        switch (followType) {
            case CommunityConstant.FOLLOW:
                url = ServerUrlConstant.FOLLOW_BLOG_AUTHOR_URI;
                break;
            case CommunityConstant.UN_FOLLOW:
                url = ServerUrlConstant.UN_FOLLOW_BLOG_AUTHOR_URI;
                break;
            default:
                Toast.makeText(MyApplication.getContext(), HintConstant.BLOG_AUTHOR_FOLLOW_ERROR,
                        Toast.LENGTH_SHORT).show();
                return;
        }

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.FOLLOW_BLOG_AUTHOR_FOLLOWER, userId)
                .add(ServerPostDataConstant.FOLLOW_BLOG_AUTHOR_FOLLOWED, authorId)
                .build();
        HttpUtil.sendHttpPost(url, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.getCallList().add(call);
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), HintConstant.BLOG_AUTHOR_FOLLOW_ERROR,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    activity.getCallList().add(call);
                    String responseText = response.body().string();
                    FollowGson followGson = JsonUtil.handleFollowResponse(responseText);
                    if (followGson.getMsg().equals(CommunityConstant.BLOG_FOLLOW_SUCCESS_RESPONSE)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (followType) {
                                    case CommunityConstant.FOLLOW:
                                        followInterface.followSucceed();
                                        break;
                                    case CommunityConstant.UN_FOLLOW:
                                        followInterface.unFollowSucceed();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    }
                    else if (followGson.getMsg().equals(CommunityConstant.BLOG_FOLLOW_ERROR_RESPONSE)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.getContext(),
                                        HintConstant.BLOG_AUTHOR_FOLLOW_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),
                                    HintConstant.BLOG_AUTHOR_FOLLOW_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 活动， 博客, 查看回复评论
     * @param activity：调用此函数的activity
     * @param requestDataInterface：请求网络刷新UI接口
     * @param contentId：活动或者博客的id
     * @param userId：用户的id
     * @param toCommentId: 回复的评论的id(若无则为-1)
     * @param content：评论内容
     * @param category：SELECT_ACTIVITY or SELECT_BLOG or SELECT_CONTEST
     */
    public static void comment(final BaseActivity activity, final RequestDataInterface requestDataInterface,
                               String contentId, String userId, String toCommentId, String content,
                               int category) {
        String categoryIdKey;
        switch (category) {
            // 活动
            case HomeConstant.SELECT_ACTIVITY:
                categoryIdKey = ServerPostDataConstant.COMMENT_ACTIVITY_ID;
                break;
            // 博客
            case HomeConstant.SELECT_BLOG:
                categoryIdKey = ServerPostDataConstant.COMMENT_BLOG_ID;
                break;
            // 竞赛
            case HomeConstant.SELECT_CONTEST:
                categoryIdKey = ServerPostDataConstant.COMMENT_CONTEST_ID;
                break;
            default:
                Toast.makeText(MyApplication.getContext(), HintConstant.COMMENT_ERROR,
                        Toast.LENGTH_SHORT).show();
                return;
        }

        FormBody formBody;
        if (toCommentId.equals(CommentConstant.COMMENT_TO_NO_ONE)) {
            formBody = new FormBody.Builder()
                    .add(categoryIdKey, contentId)
                    .add(ServerPostDataConstant.USER_ID, userId)
                    .add(ServerPostDataConstant.COMMENT_CONTENT, content)
                    .build();
        }
        else {
            formBody = new FormBody.Builder()
                    .add(ServerPostDataConstant.USER_ID, userId)
                    .add(ServerPostDataConstant.COMMENT_CONTENT, content)
                    .add(ServerPostDataConstant.COMMENT_TO_COMMENT_ID, toCommentId)
                    .build();
        }

        HttpUtil.sendHttpPost(ServerUrlConstant.COMMENT_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.getCallList().add(call);
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), HintConstant.COMMENT_ERROR,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    activity.getCallList().add(call);
                    String responseText = response.body().string();
                    CommentGson commentGson = JsonUtil.handleCommentResponse(responseText);
                    if (commentGson.getMsg().equals(CommentConstant.COMMENT_SUCCESS_RESPONSE)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, HintConstant.COMMENT_SUCCESS,
                                        Toast.LENGTH_SHORT).show();
                                requestDataInterface.requestDataFromServer(false, true);
                            }
                        });
                    }
                    else if (commentGson.getMsg().equals(CommentConstant.COMMENT_ERROR_RESPONSE)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.getContext(),
                                        HintConstant.COMMENT_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),
                                    HintConstant.COMMENT_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 修改用户信息：昵称或性别
     * @param activity：调用此函数的活动
     * @param userId：用户id
     * @param sex：SECRET or MALE or FEMALE
     * @param nickname：昵称
     * @param type：EDIT_NICKNAME or EDIT_SEX
     */
    public static void editUserInfo(MainActivity activity, String userId, int sex, String nickname,
                                    int type) {
        FormBody formBody;
        if (type == MineConstant.EDIT_NICKNAME) {
            formBody = new FormBody.Builder()
                    .add(ServerPostDataConstant.USER_INFO_USER_ID, userId)
                    .add(ServerPostDataConstant.EDIT_USER_NICKNAME, nickname)
                    .build();
        }
        else if (type == MineConstant.EDIT_SEX) {
            formBody = new FormBody.Builder()
                    .add(ServerPostDataConstant.USER_INFO_USER_ID, userId)
                    .add(ServerPostDataConstant.EDIT_USER_SEX, String.valueOf(sex))
                    .build();
        }
        else {
            Toast.makeText(MyApplication.getContext(),
                    HintConstant.EDIT_USER_INFO_ERROR, Toast.LENGTH_SHORT).show();
            return ;
        }

        HttpUtil.sendHttpPost(ServerUrlConstant.EDIT_USER_INFO_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.getCallList().add(call);
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(),
                                HintConstant.EDIT_USER_INFO_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call,@NonNull  Response response) throws IOException {
                try {
                    activity.getCallList().add(call);
                    String responseText = response.body().string();
                    EditUserInfoGson editUserInfoGson = JsonUtil.handleEditUserInfoResponse(responseText);
                    if (editUserInfoGson.getMsg().equals(MineConstant.EDIT_USER_INFO_SUCCESS)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (type == MineConstant.EDIT_NICKNAME) {
                                    activity.mineFragment.updateNicknameUI(nickname);
                                }
                                else {
                                    activity.mineFragment.updateSexUI();
                                }
                            }
                        });
                    }
                    else if (editUserInfoGson.getMsg().equals(MineConstant.EDIT_USER_INFO_ERROR)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.getContext(),
                                        HintConstant.EDIT_USER_INFO_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),
                                    HintConstant.EDIT_USER_INFO_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}






















