package com.ct.sprintnba_demo01.constant;

/**
 * Created by ct on 2017/1/18.
 *
 * ==================================
 * NBA
 * ==================================
 */

public enum Column_NBA {


    BANNER("banner"),      //头条
    NEWS("news"),          //新闻
    VIDEO("videos"),     //视频集锦
    DEPTH("depth"),        //十佳球/五佳球
    HIGHLIGHT("highlight");//赛场花絮

    private String column;

    private Column_NBA(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }


}
