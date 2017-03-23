package com.ct.sprintnba_demo01.constant;

/**
 * Created by ct on 2017/1/18.
 * <p>
 * ==================================
 * 福利
 * ==================================
 */

public enum Column_MM {

    SCHOOL_BABE("10"), //校花
    PURE("7"),         //清纯
    TEMPERAMENT("5"), //气质
    LOLITA("6"),//萌女
    WALLPAPER("4"),//壁纸
    ALTERNATIVE("8"),//非主流
    STAR("11"),//明星
    ART("13"),//美术
    HOME("15"),//家居
    MAN("17"),//型男
    PET("16");//萌宠


    private String category;

    Column_MM(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
