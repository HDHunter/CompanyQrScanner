package com.hunter.appinfomonitor.yodo1page;

import java.util.List;

public class ChannelList {

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
         * _id : 5992ce28809d899e00bab57d
         * name : 腾讯应用宝
         * code : TXYYB
         * __v : 0
         * channel_code : [{"name":"应用宝","code":"TXYYB","_id":"5a6a9ea689e6192dd4e6a722"}]
         * config_rules : [{"key":"appkey-sandbox","desc":""},{"key":"appkey-prod","desc":""}]
         * create_date : 2020-08-17T05:36:30.020Z
         * config :
         */
        private String _id;
        private String name;
        private String code;
        private int __v;
        private String create_date;
        private String config;
        private List<ChannelCodeBean> channel_code;
        private List<ConfigRulesBean> config_rules;

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }

        public List<ChannelCodeBean> getChannel_code() {
            return channel_code;
        }

        public void setChannel_code(List<ChannelCodeBean> channel_code) {
            this.channel_code = channel_code;
        }

        public List<ConfigRulesBean> getConfig_rules() {
            return config_rules;
        }

        public void setConfig_rules(List<ConfigRulesBean> config_rules) {
            this.config_rules = config_rules;
        }

        public static class ChannelCodeBean {
            /**
             * name : 应用宝
             * code : TXYYB
             * _id : 5a6a9ea689e6192dd4e6a722
             */
            private String name;
            private String code;
            private String _id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }
        }

        public static class ConfigRulesBean {
            /**
             * key : appkey-sandbox
             * desc :
             */
            private String key;
            private String desc;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
