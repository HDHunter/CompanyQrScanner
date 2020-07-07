package com.hunter.appinfomonitor.ui;

public interface OtaAPi {

    String base = "https://ota.yodo1.com";


    String login = base + "/api/user/login";

    String membersList = base + "/api/team/{}/members";

    String allAppList = base + "/api/apps/all";

    String createCount = base + "/api/count/{appid}/{versionId}";
}
