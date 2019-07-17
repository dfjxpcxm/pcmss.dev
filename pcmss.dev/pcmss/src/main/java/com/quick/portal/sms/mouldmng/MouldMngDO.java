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
package com.quick.portal.sms.mouldmng;

import java.util.Date;


/**
 * sys_mould_info数据对象
 */
public class MouldMngDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;

    public MouldMngDO(){}

    public MouldMngDO(MouldMngDO u){
        setMould_id(u.getMould_id());
        setMould_name(u.getMould_name());
        setMould_content(u.getMould_content());
        setMould_type(u.getMould_type());
        setMould_author(u.getMould_author());
        setMould_state(u.getMould_state());
        setCre_time(u.getCre_time());
        setUpd_time(u.getUpd_time());

    }
    // <editor-fold defaultstate="collapsed" desc="私有成员">


    /**
     *模板编号
     */
    private Integer  mould_id;


    private Integer  mould_num;
    /**
     *模板名称
     */
    private String  mould_name;
    /**
     *模板内容
     */
    private String  mould_content;

    /*
    * 模板类别：验证码通知/短信通知/系统通知/推广短信/群发助手
    */
    private Integer  mould_type;

    private String  apply_causes;

    private String remarks;

    /*
     *模板作者
     */
    private Integer mould_author;

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


    private Integer common_mould_id;



    public Integer getMould_id() {
        return mould_id;
    }

    public void setMould_id(Integer mould_id) {
        this.mould_id = mould_id;
    }

    public String getMould_name() {
        return mould_name;
    }

    public void setMould_name(String mould_name) {
        this.mould_name = mould_name;
    }

    public String getMould_content() {
        return mould_content;
    }

    public void setMould_content(String mould_content) {
        this.mould_content = mould_content;
    }


    public Integer getMould_author() {
        return mould_author;
    }

    public void setMould_author(Integer mould_author) {
        this.mould_author = mould_author;
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



    public String getApply_causes() {
        return apply_causes;
    }

    public void setApply_causes(String apply_causes) {
        this.apply_causes = apply_causes;
    }

    public Integer getCommon_mould_id() {
        return common_mould_id;
    }

    public void setCommon_mould_id(Integer common_mould_id) {
        this.common_mould_id = common_mould_id;
    }

    public Integer getMould_num() {
        return mould_num;
    }

    public void setMould_num(Integer mould_num) {
        this.mould_num = mould_num;
    }

    public Integer getMould_type() {
        return mould_type;
    }

    public void setMould_type(Integer mould_type) {
        this.mould_type = mould_type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}