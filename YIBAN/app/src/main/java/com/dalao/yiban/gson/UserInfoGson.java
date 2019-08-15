package com.dalao.yiban.gson;

/**
 * 用户信息的json
 */
public class UserInfoGson {

    /**
     * user : {"sex":1,"email":"mingxue@yahoo.com","level":null,"username":"林明凭","avatar ":"/static/photo/8.jpeg","nickname":"刘雪梅","phone":"15062959620","school":"scau"}
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
         * sex : 1
         * email : mingxue@yahoo.com
         * level : null
         * username : 林明凭
         * avatar  : /static/photo/8.jpeg
         * nickname : 刘雪梅
         * phone : 15062959620
         * school : scau
         */

        private int sex;
        private String email;
        private int level;
        private String username;
        private String avatar;
        private String nickname;
        private String phone;
        private String school;

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }
    }
}
