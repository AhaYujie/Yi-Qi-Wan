package com.dalao.yiban.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 竞赛首页数据的json
 */
public class ContestListGson {

    @SerializedName("contest_list")
    private List<ContestListBean> contestList;

    public List<ContestListBean> getContestList() {
        return contestList;
    }

    public void setContestList(List<ContestListBean> contestList) {
        this.contestList = contestList;
    }

    public static class ContestListBean {
        /**
         * time : 2019-08-06 13:31:38
         * pageviews : 1
         * title : 竞赛标题测试6
         * id : 6
         * author : 网站发布人6
         */

        private String time;
        private int pageviews;
        private String title;
        private int id;
        private String author;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getPageviews() {
            return pageviews;
        }

        public void setPageviews(int pageviews) {
            this.pageviews = pageviews;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
