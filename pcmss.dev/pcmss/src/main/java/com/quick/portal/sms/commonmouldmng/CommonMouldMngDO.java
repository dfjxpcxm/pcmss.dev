/**
 * <h3>标题 : potal统一门户-sys_user </h3>
 * <h3>描述 : sys_user数据对象</h3>
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
package com.quick.portal.sms.commonmouldmng;

import java.util.Date;


/**
 * sys_mould_info数据对象
 */
public class CommonMouldMngDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;

    public CommonMouldMngDO(){}


    // <editor-fold defaultstate="collapsed" desc="私有成员">


    /**
     *模板编号
     */
    private Integer  common_mould_id;


    private Integer  mould_type;

    /**
     *模板内容
     */
    private String  mould_content;

    /*

    /*
     * 状态禁用：0；启用：1
     */
    private Integer mould_state;


    /**
     *cre_time
     */
    private Date  cre_time;

    /**
     *upd_time
     */
    private Date  upd_time;


    public Integer getCommon_mould_id() {
        return common_mould_id;
    }

    public void setCommon_mould_id(Integer common_mould_id) {
        this.common_mould_id = common_mould_id;
    }

    public Integer getMould_type() {
        return mould_type;
    }

    public void setMould_type(Integer mould_type) {
        this.mould_type = mould_type;
    }

    public String getMould_content() {
        return mould_content;
    }

    public void setMould_content(String mould_content) {
        this.mould_content = mould_content;
    }

    public Integer getMould_state() {
        return mould_state;
    }

    public void setMould_state(Integer mould_state) {
        this.mould_state = mould_state;
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