package com.hunter.appinfomonitor.ui;

public interface OtaAPi {

    String base = "https://ota.yodo1.com";


    String login = base + "/api/user/login";
    String teamList = base + "/api/user/teams";
    String membersList = base + "/api/team/{}/members";

}
