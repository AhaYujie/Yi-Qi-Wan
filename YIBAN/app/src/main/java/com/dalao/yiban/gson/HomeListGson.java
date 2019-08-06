package com.dalao.yiban.gson;

import java.util.List;

/**
 * 竞赛首页数据的json
 */
public class HomeListGson {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pageviews : 1
         * time : 2019-08-06 13:31:38
         * title : 竞赛标题测试6
         * author : 网站发布人6
         * id : 6
         */

        private int pageviews;
        private String time;
        private String title;
        private String author;
        private int id;

        public int getPageviews() {
            return pageviews;
        }

        public void setPageviews(int pageviews) {
            this.pageviews = pageviews;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
