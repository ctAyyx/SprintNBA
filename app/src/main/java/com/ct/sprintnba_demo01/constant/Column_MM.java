package com.ct.sprintnba_demo01.constant;

/**
 * Created by ct on 2017/1/18.
 * <p>
 * ==================================
 * 福利
 * ==================================
 */

public enum Column_MM {

//    SCHOOL_BABE("10"), //校花
//    PURE("7"),         //清纯
//    TEMPERAMENT("5"), //气质
//    LOLITA("6"),//萌女
//    WALLPAPER("4"),//壁纸
//    ALTERNATIVE("8"),//非主流
//    STAR("11"),//明星
//    ART("13"),//美术
//    HOME("15"),//家居
//    MAN("17"),//型男
//    PET("16");//萌宠

    N1("美女+可爱"),
    N2("美女+性感"),
    N3("美女+唯美"),
    N4("美女+清纯"),
    N5("美女+高挑"),
    N6("美女+惊艳"),
    N7("美女+风情"),
    N8("美女+韩国"),
    N9("美女+女神"),
    N10("美女+粉嫩"),
    N11("美女+短发"),
    N12("美女+时尚"),
    N13("美女+天使"),
    N14("美女+摄影"),
    N15("美女+有沟必火"),
    N16("美女+小清新"),
    N17("美女+人体"),
    N18("美女+泳装");


    private String category;

    Column_MM(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
