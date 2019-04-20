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
package com.quick.portal.sms.signmng;

import java.util.Date;


/**
 * sys_sign_info数据对象
 */
public class SignMngDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;

    public SignMngDO(){}

    public SignMngDO(SignMngDO u){
        setSign_id(u.getSign_id());
        setSign_name(u.getSign_name());
        setSign_type(u.getSign_type());
        setSign_author(u.getSign_author());
        setSign_state(u.getSign_state());
        setCre_time(u.getCre_time());
        setUpd_time(u.getUpd_time());

    }
    // <editor-fold defaultstate="collapsed" desc="私有成员">





    /**
     *用户ID
     */
    private Integer  sign_id;
    /**
     *签名名称
     */
    private String  sign_name;
    /**
     *签名类别：验证码通知/短信通知/系统通知/推广短信/群发助手
     */
    private Integer  sign_type;

    /*
     * 签名人
     */
    private Integer  sign_author;

    /*
     * 禁用：0；启用：1；
     */
    private Integer sign_state;

    /**
     *cre_time
     */
    private Date  cre_time;


    /**
     *upd_time
     */
    private Date  upd_time;


    public Integer getSign_id() {
        return sign_id;
    }

    public void setSign_id(Integer sign_id) {
        this.sign_id = sign_id;
    }

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public Integer getSign_type() {
        return sign_type;
    }

    public void setSign_type(Integer sign_type) {
        this.sign_type = sign_type;
    }

    public Integer getSign_author() {
        return sign_author;
    }

    public void setSign_author(Integer sign_author) {
        this.sign_author = sign_author;
    }

    public Integer getSign_state() {
        return sign_state;
    }

    public void setSign_state(Integer sign_state) {
        this.sign_state = sign_state;
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