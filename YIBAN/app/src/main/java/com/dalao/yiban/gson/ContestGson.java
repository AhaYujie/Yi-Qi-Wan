package com.dalao.yiban.gson;

import java.util.List;

/**
 * 竞赛内容的json
 */
public class ContestGson {

    /**
     * team : [{"avatar":"/root/face/123456.png","name":"咸鱼队１","id":1,"time":"2019-08-08 22:29:12","content":"咸鱼１队简介，我们需要最帅的人40来","author":"lmp","count":40},{"avatar":"/root/face/123456222.png","name":"咸鱼队2","id":2,"time":"2019-08-08 22:29:12","content":"咸鱼２队简介，我们需要最帅的人50来","author":"lzk","count":50},{"avatar":"/root/face/123456ss.png","name":"咸鱼队3","id":3,"time":"2019-08-08 22:29:12","content":"咸鱼３队简介，我们需要最帅的人105来","author":"abc","count":105}]
     * collection : 1
     * title : 竞赛标题测试１
     * content : 竞赛内容测试１
     * author : 网站发布人1
     * time : 2019-08-08 22:29:11
     * pageviews : 0
     */

    private int collection;
    private String title;
    private String content;
    private String author;
    private String time;
    private int pageviews;
    private List<TeamBean> team;

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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

    public List<TeamBean> getTeam() {
        return team;
    }

    public void setTeam(List<TeamBean> team) {
        this.team = team;
    }

    public static class TeamBean {
        /**
         * avatar : /root/face/123456.png
         * name : 咸鱼队１
         * id : 1
         * time : 2019-08-08 22:29:12
         * content : 咸鱼１队简介，我们需要最帅的人40来
         * author : lmp
         * count : 40
         */

        private String avatar;
        private String name;
        private int id;
        private String time;
        private String content;
        private String author;
        private int count;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
