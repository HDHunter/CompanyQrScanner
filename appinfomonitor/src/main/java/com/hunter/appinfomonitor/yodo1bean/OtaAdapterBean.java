package com.hunter.appinfomonitor.yodo1bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OtaAdapterBean implements Serializable {

    private List<TeamsGroupBean> teams;

    public OtaAdapterBean(OTALoginBean loginBean) {
        super();
        teams = new ArrayList<>();
        List<OTALoginBean.DataBean.TeamsBean> teamsss = loginBean.getData().getTeams();
        for (OTALoginBean.DataBean.TeamsBean bb : teamsss) {
            TeamsGroupBean bn = new TeamsGroupBean();
            bn.setName(bb.getName());
            bn.setTeamid(bb.get_id());
            bn.setRole(bb.getRole());
            teams.add(bn);
        }
    }


    public static class TeamsGroupBean {
        private String teamid;
        private String name;
        private String role;

        public String getTeamid() {
            return teamid;
        }

        public void setTeamid(String teamid) {
            this.teamid = teamid;
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

    public List<TeamsGroupBean> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamsGroupBean> teams) {
        this.teams = teams;
    }
}
