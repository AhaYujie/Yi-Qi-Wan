package com.dalao.yiban.gson;

import java.util.List;

public class MyFollowingGson {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * avatar : /static/photo/20190828112055066b15mbg5
         * username : 爸爸
         * userid : 2
         */

        private String avatar;
        private String username;
        private int userid;
        private int isFollow;
        private boolean following;

        public boolean getFollowing() {
            return following;
        }

        public void setFollowing(boolean following) {
            this.following = following;
        }

        public int getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(int isFollow) {
            this.isFollow = isFollow;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }
    }
}
