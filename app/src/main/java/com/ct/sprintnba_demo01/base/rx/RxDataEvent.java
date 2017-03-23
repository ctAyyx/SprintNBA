package com.ct.sprintnba_demo01.base.rx;

/**
 * Created by Administrator on 16-8-23.
 */
public class RxDataEvent {
    public int type;
    public Object data;

    public RxDataEvent(int _type, Object _data) {
        this.type = _type;
        this.data = _data;
    }

    public RxDataEvent() {
    }
}
