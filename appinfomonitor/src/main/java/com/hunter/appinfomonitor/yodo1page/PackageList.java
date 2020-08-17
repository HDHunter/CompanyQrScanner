package com.hunter.appinfomonitor.yodo1page;

import java.util.List;

public class PackageList {

    private int code;
    private List<ListBean> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * _id : 5f2bef335477c40926e7a6cf
         * user_id : sunmeng
         * appkey : 2qzpQ2MGQd
         * game_version : 1.27.2
         * version_code : 12720
         * create_date : 2020-08-06T11:48:05.455Z
         * start_date : 2020-08-06T11:48:50.860Z
         * finish_date : 2020-08-06T11:53:23.328Z
         * publish_code : SYZJ01
         * publish_name : 手游之家
         * status : 2
         * address : https://bj-ali-package-download-prod.oss-cn-beijing.aliyuncs.com/builder/apk/2qzpQ2MGQd/1.27.2/200806194805/%E7%96%AF%E7%8B%82%E5%8A%A8%E7%89%A9%E5%9B%AD_%E6%89%8B%E6%B8%B8%E4%B9%8B%E5%AE%B6_200806194805.apk
         * package_name : com.yodo1.rodeo.YODO1
         * extra : N/A
         * message :
         * sign_md5 : 4E:1E:0D:4E:0F:D9:AC:9A:C3:B4:87:5A:23:8D:82:0C
         * sign_sha256 : 68:94:28:09:74:88:13:9C:DB:31:D2:A1:53:EE:E5:FF:5E:10:5F:8E:CB:29:42:DF:2E:34:2D:FC:B7:E3:FE:4B
         * sign_sha1 : 3F:3A:A5:9F:61:73:E4:89:FA:50:3D:AE:FB:84:B2:8F:FD:F2:AA:13
         * sdk_lst : [{"sdk":"yodo1_core","label":"Yodo1SDK","sdktype":"99","version":"core:v9.1.3;core_advert:v8.0.7"},{"sdk":"umeng_analytics","label":"友盟统计","sdktype":"7","version":"v6.0.9_3.609.1"},{"sdk":"talkingdata_analytics","label":"TalkingData统计","sdktype":"7","version":"v4.0.29_3.4029.1"},{"sdk":"appsflyer_cn_analytics","label":"Appsflyer国内版","sdktype":"7","version":"v4.10.2_2.41002.1"},{"sdk":"wechat_pay","label":"微信支付","sdktype":"2","version":"v6.6.4_2.664.1"},{"sdk":"alipay_pay","label":"支付宝","sdktype":"2","version":"v15.7.6_2.1576.1"},{"sdk":"share_system","label":"系统分享","sdktype":"8","version":"v2.0.0"},{"sdk":"gdt_advert","label":"广点通广告","sdktype":"9","version":"v4.230.1100_8.42301100.1"},{"sdk":"toutiao_advert","label":"今日头条广告","sdktype":"9","version":"v3.0.0.4_8.3004.1"}]
         * releaseState : true
         * online_date : 2020-08-10
         * audit_date : 2020-08-06
         */

        private String _id;
        private String user_id;
        private String appkey;
        private String game_version;
        private String version_code;
        private String create_date;
        private String start_date;
        private String finish_date;
        private String publish_code;
        private String publish_name;
        private String status;
        private String address;
        private String package_name;
        private String extra;
        private String message;
        private String sign_md5;
        private String sign_sha256;
        private String sign_sha1;
        private boolean releaseState;
        private String online_date;
        private String audit_date;
        private List<SdkLstBean> sdk_lst;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getAppkey() {
            return appkey;
        }

        public void setAppkey(String appkey) {
            this.appkey = appkey;
        }

        public String getGame_version() {
            return game_version;
        }

        public void setGame_version(String game_version) {
            this.game_version = game_version;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getFinish_date() {
            return finish_date;
        }

        public void setFinish_date(String finish_date) {
            this.finish_date = finish_date;
        }

        public String getPublish_code() {
            return publish_code;
        }

        public void setPublish_code(String publish_code) {
            this.publish_code = publish_code;
        }

        public String getPublish_name() {
            return publish_name;
        }

        public void setPublish_name(String publish_name) {
            this.publish_name = publish_name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSign_md5() {
            return sign_md5;
        }

        public void setSign_md5(String sign_md5) {
            this.sign_md5 = sign_md5;
        }

        public String getSign_sha256() {
            return sign_sha256;
        }

        public void setSign_sha256(String sign_sha256) {
            this.sign_sha256 = sign_sha256;
        }

        public String getSign_sha1() {
            return sign_sha1;
        }

        public void setSign_sha1(String sign_sha1) {
            this.sign_sha1 = sign_sha1;
        }

        public boolean isReleaseState() {
            return releaseState;
        }

        public void setReleaseState(boolean releaseState) {
            this.releaseState = releaseState;
        }

        public String getOnline_date() {
            return online_date;
        }

        public void setOnline_date(String online_date) {
            this.online_date = online_date;
        }

        public String getAudit_date() {
            return audit_date;
        }

        public void setAudit_date(String audit_date) {
            this.audit_date = audit_date;
        }

        public List<SdkLstBean> getSdk_lst() {
            return sdk_lst;
        }

        public void setSdk_lst(List<SdkLstBean> sdk_lst) {
            this.sdk_lst = sdk_lst;
        }

        public static class SdkLstBean {
            /**
             * sdk : yodo1_core
             * label : Yodo1SDK
             * sdktype : 99
             * version : core:v9.1.3;core_advert:v8.0.7
             */

            private String sdk;
            private String label;
            private String sdktype;
            private String version;

            public String getSdk() {
                return sdk;
            }

            public void setSdk(String sdk) {
                this.sdk = sdk;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getSdktype() {
                return sdktype;
            }

            public void setSdktype(String sdktype) {
                this.sdktype = sdktype;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }
        }
    }
}
