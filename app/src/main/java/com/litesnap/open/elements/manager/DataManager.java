package com.litesnap.open.elements.manager;

import com.litesnap.open.elements.R;
import com.litesnap.open.elements.bean.HolderBean;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static List<HolderBean> mDataList;

    public static List<HolderBean> getDataList() {
        if (mDataList == null){
            mDataList = new ArrayList<>();
            mDataList.add(new HolderBean(R.drawable.horror));
            mDataList.add(new HolderBean(R.drawable.humanities));
            mDataList.add(new HolderBean(R.drawable.iq));
            mDataList.add(new HolderBean(R.drawable.joke));
            mDataList.add(new HolderBean(R.drawable.legend));
            mDataList.add(new HolderBean(R.drawable.movie));
            mDataList.add(new HolderBean(R.drawable.musci));
            mDataList.add(new HolderBean(R.drawable.natural));
        }
        return mDataList;
    }
}
