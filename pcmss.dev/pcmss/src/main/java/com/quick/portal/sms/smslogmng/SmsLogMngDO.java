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
package com.quick.portal.sms.smslogmng;

import java.util.Date;


/**
 * sys_glabal_parm数据对象
 */
public class SmsLogMngDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;

    public SmsLogMngDO(){}

    public SmsLogMngDO(SmsLogMngDO u){
        setSms_log_id(u.getSms_log_id());
        setSend_id(u.getSend_id());
        setSms_log_title(u.getSms_log_title());
        setSms_log_content(u.getSms_log_content());
        setSms_author(u.getSms_author());
        setSms_rec_obj(u.getSms_rec_obj());
        setSms_state(u.getSms_state());
        setCre_time(u.getCre_time());
        setUpd_time(u.getUpd_time());

    }
    // <editor-fold defaultstate="collapsed" desc="私有成员">

    /**
     *短信日志编号
     */
    private Integer  sms_log_id;
    /*
     * 短信编号
     */
    private Integer  send_id;
    /**
     *短信日志标题
     */
    private String  sms_log_title;
    /**
     *短信日志内容
     */
    private String  sms_log_content;
    /*
     * 短信作者
     */
    private int sms_author;

    /*
     * 短信接受者
     */
    private String  sms_rec_obj;
    /*
     * 状态失败：0；成功：1；发送中:2
     */
    private Integer  sms_state;
    /**
     *cre_time
     */
    private Date  cre_time;

    /**
     *upd_time
     */

    private Date  upd_time;


    public Integer getSms_log_id() {
        return sms_log_id;
    }

    public void setSms_log_id(Integer sms_log_id) {
        this.sms_log_id = sms_log_id;
    }

    public Integer getSend_id() {
        return send_id;
    }

    public void setSend_id(Integer send_id) {
        this.send_id = send_id;
    }

    public String getSms_log_title() {
        return sms_log_title;
    }

    public void setSms_log_title(String sms_log_title) {
        this.sms_log_title = sms_log_title;
    }

    public String getSms_log_content() {
        return sms_log_content;
    }

    public void setSms_log_content(String sms_log_content) {
        this.sms_log_content = sms_log_content;
    }

    public int getSms_author() {
        return sms_author;
    }

    public void setSms_author(int sms_author) {
        this.sms_author = sms_author;
    }

    public String getSms_rec_obj() {
        return sms_rec_obj;
    }

    public void setSms_rec_obj(String sms_rec_obj) {
        this.sms_rec_obj = sms_rec_obj;
    }

    public Integer getSms_state() {
        return sms_state;
    }

    public void setSms_state(Integer sms_state) {
        this.sms_state = sms_state;
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