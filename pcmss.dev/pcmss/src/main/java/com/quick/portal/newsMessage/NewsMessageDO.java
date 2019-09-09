/**
 * <h3>标题 : potal统一门户-section </h3>
 * <h3>描述 : section数据对象</h3>
 * <h3>日期 : 2018-05-03</h3>
 * <h3>版权 : Copyright (C) 北京东方金信科技有限公司</h3>
 * 
 * <p>
 * @author 你自己的姓名 mazong@seaboxdata.com
 * @version <b>v1.0.0</b>
 *
 * <b>修改历史:</b>
 * -------------------------------------------
 * 修改人 修改日期 修改描述
 * -------------------------------------------
 *
 *
 * </p>
 */
package com.quick.portal.newsMessage;


import java.sql.Date;

/**
 * section数据对象
 */
public class NewsMessageDO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;


    private int resource_cd;
    private String resource_id;
    private String resource_name;
    private String provider_cd;
    private String check_user;
    private String apply_user;
    private int resource_status;
    private String date_time;
    private Date cre_time;
    private Date upd_time;

    public int getResource_cd() {
        return resource_cd;
    }

    public void setResource_cd(int resource_cd) {
        this.resource_cd = resource_cd;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getProvider_cd() {
        return provider_cd;
    }

    public void setProvider_cd(String provider_cd) {
        this.provider_cd = provider_cd;
    }

    public String getCheck_user() {
        return check_user;
    }

    public void setCheck_user(String check_user) {
        this.check_user = check_user;
    }

    public String getApply_user() {
        return apply_user;
    }

    public void setApply_user(String apply_user) {
        this.apply_user = apply_user;
    }

    public int getResource_status() {
        return resource_status;
    }

    public void setResource_status(int resource_status) {
        this.resource_status = resource_status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public Date getCre_time() {
        return cre_time;
    }

    public void setCre_time(Date cre_time) {
        this.cre_time = cre_time;
    }

    public Date getUpd_time() {
        return upd_time;
    }

    public void setUpd_time(Date upd_time) {
        this.upd_time = upd_time;
    }
}

