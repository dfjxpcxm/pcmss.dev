package com.quick.portal.web.mainframe;

import java.io.Serializable;

public class AcessData2MainBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int app_count;

    private int user_count;
    private int index_count;
    private int job_count;


    public int getJob_count() {
        return job_count;
    }

    public void setJob_count(int job_count) {
        this.job_count = job_count;
    }

    public int getIndex_count() {
        return index_count;
    }

    public void setIndex_count(int index_count) {
        this.index_count = index_count;
    }

    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    public int getApp_count() {
        return app_count;
    }

    public void setApp_count(int app_count) {
        this.app_count = app_count;
    }
}
