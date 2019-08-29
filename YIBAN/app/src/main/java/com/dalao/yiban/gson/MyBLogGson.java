package com.dalao.yiban.gson;

import java.util.List;

public class MyBLogGson {

    private List<MyblogsBean> myblogs;

    public List<MyblogsBean> getMyblogs() {
        return myblogs;
    }

    public void setMyblogs(List<MyblogsBean> myblogs) {
        this.myblogs = myblogs;
    }

    public static class MyblogsBean {
        /**
         * id : 1
         * title : 博客标题
         * time : 2019-08-28 10:50:54
         * avatar : /static/photo/2019082815043601fhfp1gh7
         * pageviews : 2
         */

        private int id;
        private String title;
        private String time;
        private String avatar;
        private int pageviews;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getPageviews() {
            return pageviews;
        }

        public void setPageviews(int pageviews) {
            this.pageviews = pageviews;
        }
    }
}
