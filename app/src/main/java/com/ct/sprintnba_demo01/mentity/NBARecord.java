package com.ct.sprintnba_demo01.mentity;

import java.util.ArrayList;

/**
 * Created by ct on 2017/3/6.
 * ===================
 * 球队战绩实体类
 * ===================
 */

public class NBARecord {

    public String title;
    public int type;

    public ArrayList<String> head;
    public ArrayList<Team> rows;

    public Team team;


    public static class Team {

       /* {
            "teamId": "17",
                "name": "\u7bee\u7f51",
                "badge": "http:\/\/mat1.gtimg.com\/sports\/nba\/logo\/1602\/17.png",
                "serial": "15",
                "color": "1",
                "detailUrl": "http:\/\/sports.qq.com\/kbsweb\/kbsshare\/team.htm?ref=nbaapp&cid=100000&tid=17"
        },
                "10",
                "51",
                "16%",
                "31.5"*/


        public int teamId;
        public String name;
        public String badge;
        public String serial;
        public String color;
        public String detailUrl;

        public int wins;//胜场
        public int defeata;//败场
        public String winRate;//胜率
        public double winDifference;//胜场差

    }

}
