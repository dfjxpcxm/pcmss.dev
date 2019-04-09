/**
 * <h3>标题 : potal统一门户-InterfaceApplication </h3>
 * <h3>描述 : InterfaceApplication数据对象</h3>
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
package com.quick.portal.interfaceManage.interfaceApplication;

import java.util.Date;


/**
 * user_department数据对象
 */
public class InterfaceApplicationDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
	
    // <editor-fold defaultstate="collapsed" desc="私有成员">
    //申请者字段
    private String app_id;
    private String app_usename;
    private String app_reason;
    private Integer inter_id;
    private Date cre_time;
    private Date upd_time;

    //
    public Integer getInter_id() {
        return inter_id;
    }

    public void setInter_id(Integer inter_id) {
        this.inter_id = inter_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_usename() {
        return app_usename;
    }

    public void setApp_usename(String app_usename) {
        this.app_usename = app_usename;
    }

    public String getApp_reason() {
        return app_reason;
    }

    public void setApp_reason(String app_reason) {
        this.app_reason = app_reason;
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