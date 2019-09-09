package com.dalao.yiban.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 用户信息的json
 */
public class UserInfoGson {


    /**
     * user : {"avatar":"/static/photo/8.jpeg","sex":3,"school":"scau","nickname":"吴芳","username":"为司马"}
     */

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * avatar : /static/photo/8.jpeg
         * sex : 2
         * school : scau
         * nickname : 吴芳
         * username : 为司马
         */
        private String avatar;
        private int sex;
        private String school;
        private String nickname;
        private String username;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
