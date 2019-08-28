package com.dalao.yiban.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommentConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.ActivityGson;
import com.dalao.yiban.gson.CommentBean;
import com.dalao.yiban.gson.CommentsGson;
import com.dalao.yiban.my_interface.CommentInterface;
import com.dalao.yiban.ui.activity.ActivityActivity;
import com.dalao.yiban.ui.activity.BaseActivity;
import com.dalao.yiban.ui.activity.ViewReplyActivity;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
    implements View.OnClickListener {

    private CommentInterface commentInterface;

    private BaseActivity activity;

    private List<CommentBean> commentsBeanList;

    private String userId;

    private String contentId;

    private int category;

    private int page;       // 当前页数

    private int COMMENTS_NUMBER_PER_PAGE = 10;  // 每页评论数量

    private boolean isUpdating; // 是否正在进行网络请求

    private boolean isMoreComments; // 是否有更多的评论可以获取

    private CommentsLoadingLayout commentsLoadingLayout;

    public List<CommentBean> getCommentsBeanList() {
        return commentsBeanList;
    }

    public void setCommentsBeanList(List<CommentBean> commentsBeanList) {
        this.commentsBeanList = commentsBeanList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView commentFace;
        TextView commentPersonName;
        TextView commentText;
        TextView commentTime;
        Button commentReplyButton;
        TextView commentReplyNum;
        LinearLayout commentReply;

        private ViewHolder(View view) {
            super(view);
            commentFace = (ImageView) view.findViewById(R.id.comment_author_face);
            commentPersonName = (TextView) view.findViewById(R.id.comment_person_name);
            commentText = (TextView) view.findViewById(R.id.comment_text);
            commentTime = (TextView) view.findViewById(R.id.comment_time);
            commentReplyButton = (Button) view.findViewById(R.id.comment_reply_button);
            commentReplyNum = (TextView) view.findViewById(R.id.comment_reply_num);
            commentReply = (LinearLayout) view.findViewById(R.id.comment_reply);
        }

    }

    /**
     *
     * @param activity :
     * @param commentInterface :
     * @param userId : 用户id
     * @param contentId : 活动或竞赛或博客的id
     * @param category : SELECT_ACTIVITY or SELECT_BLOG or SELECT_CONTEST
     */
    public CommentAdapter(BaseActivity activity, CommentInterface commentInterface, String userId,
                          String contentId, int category, CommentsLoadingLayout commentsLoadingLayout) {
        this.activity = activity;
        this.commentInterface = commentInterface;
        this.userId = userId;
        this.contentId = contentId;
        this.category = category;
        this.page = 1;
        this.isUpdating = false;
        this.isMoreComments = true;
        this.commentsLoadingLayout = commentsLoadingLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CommentBean commentBean = commentsBeanList.get(position);
        holder.commentPersonName.setText(commentBean.getAuthor());
        holder.commentText.setText(commentBean.getContent());
        holder.commentTime.setText(commentBean.getTime());
        Glide.with(MyApplication.getContext())
                .load(ServerUrlConstant.SERVER_URI + commentBean.getAvatar())
                .placeholder(R.drawable.ic_avatar_grey)
                .error(R.drawable.ic_avatar_grey)
                .fallback(R.drawable.ic_avatar_grey)
                .into(holder.commentFace);
        if (commentBean.getNumber() == 0) {
            holder.commentReply.setVisibility(View.GONE);
        }
        else {
            holder.commentReply.setVisibility(View.VISIBLE);
            holder.commentReplyNum.setText(String.valueOf(commentBean.getNumber()));
            holder.commentReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 启动查看回复activity
                    ViewReplyActivity.actionStart(activity, commentBean, userId, contentId, category);
                }
            });
        }

        // 设置点击事件
        holder.commentPersonName.setOnClickListener(this);
        holder.commentFace.setOnClickListener(this);
        holder.commentReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 游客禁止使用此功能
                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    commentInterface.editCommentText(String.valueOf(commentBean.getId()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsBeanList != null ? commentsBeanList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_person_name:
            case R.id.comment_author_face:
                // 取消此功能
                break;
            default:
                break;
        }
    }

    /**
     * 加载更多评论
     * @param url：
     * @param contentIdKey：ACTIVITY_ID or CONTEST_ID or BLOG_ID
     * @param contentId :
     */
    public void loadMoreComments(String url, String contentIdKey, String contentId) {
        // 正在网络请求或者无更多评论则不进行加载
        if (isUpdating || !isMoreComments) {
            return;
        }
        startUpdating();
        commentsLoadingLayout.showProgressBar();

        int newPage = getLoadPage();

        FormBody formBody  = new FormBody.Builder()
                .add(contentIdKey, contentId)
                .add(ServerPostDataConstant.USER_ID, userId)
                .add(ServerPostDataConstant.PAGE, String.valueOf(newPage))
                .build();

        Call call = HttpUtil.sendHttpPost(url, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endUpdating();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.body() == null) {
                        throw new NullPointerException();
                    }
                    String responseText = response.body().string();
                    final CommentsGson commentsGson = JsonUtil.handleCommentsResponse(responseText);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commentsGson == null) {
                                throw new NullPointerException();
                            }
                            // 无更多评论
                            if (!isNewComments(commentsGson.getComments(), newPage)) {
                                isMoreComments = false;
                                commentsLoadingLayout.showNoMoreComments();
                            }
                            // 添加新的评论
                            else {
                                addComments(commentsGson.getComments(), newPage);
                                CommentAdapter.this.notifyDataSetChanged();
                                page = newPage;
                            }
                        }
                    });
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                finally {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            endUpdating();
                        }
                    });
                }
            }
        });
        activity.getCallList().add(call);
    }

    /**
     * 评论后请求获得新评论
     * @param url：
     * @param contentIdKey：ACTIVITY_ID or CONTEST_ID or BLOG_ID
     * @param contentId :
     * @param toCommentId : 回复的评论的id
     */
    public void loadAfterComment(String url, String contentIdKey, String contentId, String toCommentId) {
        isMoreComments = true;
        commentsLoadingLayout.closeNoMoreComments();
        loadMoreComments(url, contentIdKey, contentId);
        // 如果回复别人的评论则更新UI
        if (!toCommentId.equals(CommentConstant.COMMENT_TO_NO_ONE)) {
            for (CommentBean commentBean : commentsBeanList) {
                if (commentBean.getId() == Integer.valueOf(toCommentId)) {
                    commentBean.setNumber(commentBean.getNumber() + 1);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    private void startUpdating() {
        this.isUpdating = true;
        commentsLoadingLayout.showProgressBar();
    }

    private void endUpdating() {
        this.isUpdating = false;
        commentsLoadingLayout.closeProgressBar();
    }

    public interface CommentsLoadingLayout {
        // 显示进度条
        void showProgressBar();

        // 关闭进度条
        void closeProgressBar();

        // 显示无更多评论
        void showNoMoreComments();

        // 隐藏无更多评论
        void closeNoMoreComments();
    }

    /**
     * 获取要加载的页数
     * @return :
     */
    private int getLoadPage() {
        if (commentsBeanList.size() != 0 && (commentsBeanList.size() % COMMENTS_NUMBER_PER_PAGE == 0)) {
            return page + 1;
        }
        else {
            return page;
        }
    }

    /**
     * 添加新的评论
     * @param commentsBeanList：获取的新评论
     * @param loadPage：新评论所在的页数
     */
    private void addComments(List<CommentBean> commentsBeanList, int loadPage) {
        // 加载新的页面的评论，无重复
        if (loadPage != page) {
            this.commentsBeanList.addAll(commentsBeanList);
        }
        // 加载原页面的新评论，有重复需去重
        else {
            int start = (this.commentsBeanList.size() % COMMENTS_NUMBER_PER_PAGE);
            for (; start < commentsBeanList.size(); start++) {
                this.commentsBeanList.add(commentsBeanList.get(start));
            }
        }
    }

    /**
     * 判读获得的新评论是否为新数据
     * @param commentsBeanList：新评论
     * @param loadPage：新评论所在的页数
     * @return ：true则为新数据，否则false
     */
    private boolean isNewComments(List<CommentBean> commentsBeanList, int loadPage) {
        // 加载新的页面的评论
        if (loadPage != page) {
            return !commentsBeanList.isEmpty();
        }
        // 加载原页面的新评论
        else {
            return (this.commentsBeanList.size() % COMMENTS_NUMBER_PER_PAGE) <
                    commentsBeanList.size();
        }
    }

}
