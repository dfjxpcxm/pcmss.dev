package com.quick.portal.web.mainframe;

import java.io.Serializable;

public class JobDataBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int job_fail_count;

    private int job_suc_count;


    private int login_count;

    public int getJob_fail_count() {
        return job_fail_count;
    }

    public void setJob_fail_count(int job_fail_count) {
        this.job_fail_count = job_fail_count;
    }

    public int getJob_suc_count() {
        return job_suc_count;
    }

    public void setJob_suc_count(int job_suc_count) {
        this.job_suc_count = job_suc_count;
    }

    public int getLogin_count() {
        return login_count;
    }

    public void setLogin_count(int login_count) {
        this.login_count = login_count;
    }
}
