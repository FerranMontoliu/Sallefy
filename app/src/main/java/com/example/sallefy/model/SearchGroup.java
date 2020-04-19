package com.example.sallefy.model;

import java.util.ArrayList;
import java.util.List;

public class SearchGroup {
    private String mGroupName;
    private Object mList;

    public SearchGroup(String mGroupName, Object mList) {
        this.mGroupName = mGroupName;
        this.mList = mList;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public Object getList() {
        return mList;
    }

    public void setList(Object mList) {
        this.mList = mList;
    }
}
