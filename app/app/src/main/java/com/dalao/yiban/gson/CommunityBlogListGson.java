package com.dalao.yiban.gson;

import java.util.List;

/**
 * 社区博客列表的json
 */
public class CommunityBlogListGson {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * time : 2019-08-11 20:35:24
         * authorid : 98
         * avatar : /static/photo/8.jpeg
         * author : jieqiao
         * pageviews : 125
         * title : 语言还是其中一起学校.
         * id : 98
         */

        private String time;
        private int authorid;
        private String avatar;
        private String author;
        private int pageviews;
        private String title;
        private int id;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getAuthorid() {
            return authorid;
        }

        public void setAuthorid(int authorid) {
            this.authorid = authorid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
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
    }
}
