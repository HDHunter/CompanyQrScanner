package com.hunter.appinfomonitor.yodo1bean;

import java.io.Serializable;
import java.util.List;

public class OTALoginBean implements Serializable {

    private boolean success;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private boolean canChangePassword;
        private boolean allTeamIndex;
        private boolean loginIndex;
        private boolean appUploadIndex;
        private boolean appDownloadIndex;
        private boolean appDeleteIndex;
        private boolean publishPageIndex;
        private boolean publishOssIndex;
        private boolean teamCreateIndex;
        private boolean teamAddMemberIndex;
        private boolean teamDeleteIndex;
        private boolean usersManager;
        private boolean adminIndex;
        private String _id;
        private String username;
        private String password;
        private String email;
        private int __v;
        private String userToken;
        private String token;
        private List<TeamsBean> teams;
        private List<?> feedbacks;

        public boolean isCanChangePassword() {
            return canChangePassword;
        }

        public void setCanChangePassword(boolean canChangePassword) {
            this.canChangePassword = canChangePassword;
        }

        public boolean isAllTeamIndex() {
            return allTeamIndex;
        }

        public void setAllTeamIndex(boolean allTeamIndex) {
            this.allTeamIndex = allTeamIndex;
        }

        public boolean isLoginIndex() {
            return loginIndex;
        }

        public void setLoginIndex(boolean loginIndex) {
            this.loginIndex = loginIndex;
        }

        public boolean isAppUploadIndex() {
            return appUploadIndex;
        }

        public void setAppUploadIndex(boolean appUploadIndex) {
            this.appUploadIndex = appUploadIndex;
        }

        public boolean isAppDownloadIndex() {
            return appDownloadIndex;
        }

        public void setAppDownloadIndex(boolean appDownloadIndex) {
            this.appDownloadIndex = appDownloadIndex;
        }

        public boolean isAppDeleteIndex() {
            return appDeleteIndex;
        }

        public void setAppDeleteIndex(boolean appDeleteIndex) {
            this.appDeleteIndex = appDeleteIndex;
        }

        public boolean isPublishPageIndex() {
            return publishPageIndex;
        }

        public void setPublishPageIndex(boolean publishPageIndex) {
            this.publishPageIndex = publishPageIndex;
        }

        public boolean isPublishOssIndex() {
            return publishOssIndex;
        }

        public void setPublishOssIndex(boolean publishOssIndex) {
            this.publishOssIndex = publishOssIndex;
        }

        public boolean isTeamCreateIndex() {
            return teamCreateIndex;
        }

        public void setTeamCreateIndex(boolean teamCreateIndex) {
            this.teamCreateIndex = teamCreateIndex;
        }

        public boolean isTeamAddMemberIndex() {
            return teamAddMemberIndex;
        }

        public void setTeamAddMemberIndex(boolean teamAddMemberIndex) {
            this.teamAddMemberIndex = teamAddMemberIndex;
        }

        public boolean isTeamDeleteIndex() {
            return teamDeleteIndex;
        }

        public void setTeamDeleteIndex(boolean teamDeleteIndex) {
            this.teamDeleteIndex = teamDeleteIndex;
        }

        public boolean isUsersManager() {
            return usersManager;
        }

        public void setUsersManager(boolean usersManager) {
            this.usersManager = usersManager;
        }

        public boolean isAdminIndex() {
            return adminIndex;
        }

        public void setAdminIndex(boolean adminIndex) {
            this.adminIndex = adminIndex;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<TeamsBean> getTeams() {
            return teams;
        }

        public void setTeams(List<TeamsBean> teams) {
            this.teams = teams;
        }

        public List<?> getFeedbacks() {
            return feedbacks;
        }

        public void setFeedbacks(List<?> feedbacks) {
            this.feedbacks = feedbacks;
        }

        public static class TeamsBean implements Serializable {
            /**
             * _id : 5e672afeed939f001845ba46
             * name : 我的团队
             * role : owner
             */

            private String _id;
            private String name;
            private String role;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }
        }
    }
}
