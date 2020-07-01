package com.hunter.appinfomonitor.yodo1bean;

import java.util.List;

public class OtaMemberListBean {

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

    public static class DataBean {
        private boolean isArchiveTeam;
        private String _id;
        private String name;
        private String createAt;
        private String creatorId;
        private int __v;
        private String allPackageTags;
        private List<MembersBean> members;

        public boolean isIsArchiveTeam() {
            return isArchiveTeam;
        }

        public void setIsArchiveTeam(boolean isArchiveTeam) {
            this.isArchiveTeam = isArchiveTeam;
        }

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

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getAllPackageTags() {
            return allPackageTags;
        }

        public void setAllPackageTags(String allPackageTags) {
            this.allPackageTags = allPackageTags;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
        }

        public static class MembersBean {

            private String _id;
            private String username;
            private String email;
            private String role;

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

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
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
