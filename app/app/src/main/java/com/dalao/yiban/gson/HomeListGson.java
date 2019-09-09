package com.dalao.yiban.gson;

import java.util.List;

/**
 * 首页竞赛和活动列表的json
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
         * time : 2019-08-21 20:27:06
         * url : https://cdn.huodongxing.com/logo/201907/2503511999800/783513570086632_v2small.jpg
         * pageviews : 0
         * title : B2B企业科学的销售增长之道 | 第二期
         * author : https://www.huodongxing.com/event/2503511999800?utm_source=%e6%b4%bb%e5%8a%a8%e4%ba%ba%e6%b0%94%e6%8e%92%e8%a1%8c%e6%a6%9c&utm_medium=%e6%b4%bb%e5%8a%a8%e4%ba%ba%e6%b0%94%e6%8e%92%e8%a1%8c%e6%a6%9c&utm_campaign=ranklistpage&qd=6628363131252
         * id : 39
         */

        private String time;
        private String url;
        private int pageviews;
        private String title;
        private String author;
        private int id;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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
