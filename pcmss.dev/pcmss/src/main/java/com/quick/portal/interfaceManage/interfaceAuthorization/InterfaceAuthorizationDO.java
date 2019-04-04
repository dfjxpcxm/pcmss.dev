/**
 * <h3>标题 : potal统一门户-InterfaceAuthorization</h3>
 * <h3>描述 : InterfaceAuthorization数据对象</h3>
 * <h3>日期 : 2018-04-13</h3>
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
package com.quick.portal.interfaceManage.interfaceAuthorization;

import java.util.Date;


/**
 * user_department数据对象
 */
public class InterfaceAuthorizationDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
	
    // <editor-fold defaultstate="collapsed" desc="私有成员">
    private Integer inter_id;
    private String inter_name;
    private String inter_path;
    private String inter_desc;
    private String inter_state;
    private String operator;
    private String category;
    private Date cre_time;
    private Date upd_time;


    public Integer getInter_id() {
        return inter_id;
    }

    public void setInter_id(Integer inter_id) {
        this.inter_id = inter_id;
    }

    public String getInter_name() {
        return inter_name;
    }

    public void setInter_name(String inter_name) {
        this.inter_name = inter_name;
    }

    public String getInter_path() {
        return inter_path;
    }

    public void setInter_path(String inter_path) {
        this.inter_path = inter_path;
    }

    public String getInter_desc() {
        return inter_desc;
    }

    public void setInter_desc(String inter_desc) {
        this.inter_desc = inter_desc;
    }

    public String getInter_state() {
        return inter_state;
    }

    public void setInter_state(String inter_state) {
        this.inter_state = inter_state;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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