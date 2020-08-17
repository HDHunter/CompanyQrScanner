package com.hunter.appinfomonitor.yodo1page;

import java.util.List;

public class GameList {

    private int code;
    private List<GamesBean> games;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<GamesBean> getGames() {
        return games;
    }

    public void setGames(List<GamesBean> games) {
        this.games = games;
    }

    public static class GamesBean {
        /**
         * name : {"ch":"疯狂动物园-新国内安卓广告","en":"Rodeo stampede-CP Android Ad"}
         * _id : 5996a171a26c516d8b05147c
         * appkey : 1BOq2w2wHT
         * platform : android
         * bussines_type : Yodo1
         */

        private NameBean name;
        private String _id;
        private String appkey;
        private String platform;
        private String bussines_type;

        public NameBean getName() {
            return name;
        }

        public void setName(NameBean name) {
            this.name = name;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAppkey() {
            return appkey;
        }

        public void setAppkey(String appkey) {
            this.appkey = appkey;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getBussines_type() {
            return bussines_type;
        }

        public void setBussines_type(String bussines_type) {
            this.bussines_type = bussines_type;
        }

        public static class NameBean {
            /**
             * ch : 疯狂动物园-新国内安卓广告
             * en : Rodeo stampede-CP Android Ad
             */

            private String ch;
            private String en;

            public String getCh() {
                return ch;
            }

            public void setCh(String ch) {
                this.ch = ch;
            }

            public String getEn() {
                return en;
            }

            public void setEn(String en) {
                this.en = en;
            }
        }
    }
}
