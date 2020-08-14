package com.hunter.appinfomonitor.ui;

public interface OtaAPi {

    String base = "https://ota.yodo1.com";
    String pabase = "https://pa.yodo1.com/api";

    /**
     * ota api
     */
    String login = base + "/api/user/login";

    String membersList = base + "/api/team/{}/members";

    String allAppList = base + "/api/apps/all";

    String createCount = base + "/api/count/{appid}/{versionId}";


    /**
     * pa list
     */
    String palogin = pabase + "/login";

    String gamelist = pabase + "/gameList";

    String versionList = pabase + "/versionList";

    String channelList = pabase + "/promotionChannelList";

    String packagelist = pabase + "/packageList";
}
