package com.hunter.appinfomonitor.yodo1page;

import java.util.List;

public class VersionList {

    private int code;
    private List<VersionsBean> versions;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<VersionsBean> getVersions() {
        return versions;
    }

    public void setVersions(List<VersionsBean> versions) {
        this.versions = versions;
    }

    public static class VersionsBean {
        /**
         * createDate : 2020-08-17T05:25:08.349Z
         * version : 1.232323.1
         * _id : 59ad22e959f5df19931469da
         * code : 1
         */

        private String createDate;
        private String version;
        private String _id;
        private String code;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
