package com.litesnap.open.elements.bean;

import java.util.UUID;

public class HolderBean {
    private long uuid;
    private int resId;

    public HolderBean(int resId){
        this.resId = resId;
        uuid = UUID.randomUUID().getLeastSignificantBits();
    }

    public int getResId() {
        return resId;
    }

    public long getUUID() {
        return uuid;
    }
}
