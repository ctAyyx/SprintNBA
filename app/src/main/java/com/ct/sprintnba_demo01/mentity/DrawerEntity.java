package com.ct.sprintnba_demo01.mentity;

/**
 * Created by Administrator on 2017/1/12.
 */

public class DrawerEntity {
    private int imgId;
    private String text;
    public DrawerType tag;
    private int position;

    public DrawerEntity() {
    }

    public DrawerEntity(int imgId, String text) {
        this.imgId = imgId;
        this.text = text;
    }


    public DrawerEntity(int imgId, String text, DrawerType tag) {
        this.imgId = imgId;
        this.text = text;
        this.tag = tag;

    }

    public DrawerEntity(int imgId, String text, DrawerType tag, int position) {
        this.imgId = imgId;
        this.text = text;
        this.tag = tag;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public enum DrawerType {
        NBA("nba"),
        MM("mm"),
        MUSIC("music"),
        CACHE("cache"),
        NIGHT("night");

        private String type;

        DrawerType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
