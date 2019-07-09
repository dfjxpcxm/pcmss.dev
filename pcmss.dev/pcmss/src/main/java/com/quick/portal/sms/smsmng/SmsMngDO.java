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
package com.quick.portal.sms.smsmng;

import java.util.Date;


/**
 * sys_glabal_parm数据对象
 */
public class SmsMngDO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public SmsMngDO(){}

    public SmsMngDO(SmsMngDO u){
        setSms_id(u.getSms_id());
        setSign_id(u.getSign_id());
        setSms_title(u.getSms_title());
        setSms_content(u.getSms_content());
        setSms_rec_obj(u.getSms_rec_obj());
        setSms_author(u.getSms_author());
        setSms_state(u.getSms_state());
        setMould_id(u.getMould_id());
        setCre_time(u.getCre_time());
        setUpd_time(u.getUpd_time());

    }
    // <editor-fold defaultstate="collapsed" desc="私有成员">
    /**
     *短信编号
     */
    private Integer  sms_id;

    /*
     * 签名编号
     */
    private Integer  sign_id;
    /**
     *短信标题
     */
    private String  sms_title;
    /**
     *短信内容
     */
    private String  sms_content;

    private String  mould_content;
    private String  mould_fields;

    /*
     * 短信接受者：手机段/自定义号
     */
    private String sms_rec_obj;


    /*
     *短信作者
     */
    private Integer  sms_author;

    /**
     *状态：失败：0；成功：1；发送中:2
     */
    private Integer  sms_state;
    /*
     * 模板编号
     */
    private Integer  mould_id;

    /**
     *cre_time
     */
    private Date  cre_time;


    /**
     *upd_time
     */
    private Date  upd_time;

    private Integer mun_id;

    private String phone_num;

    private String filePath;

    public Integer getSms_id() {
        return sms_id;
    }

    public void setSms_id(Integer sms_id) {
        this.sms_id = sms_id;
    }

    public Integer getSign_id() {
        return sign_id;
    }

    public void setSign_id(Integer sign_id) {
        this.sign_id = sign_id;
    }

    public String getSms_title() {
        return sms_title;
    }

    public void setSms_title(String sms_title) {
        this.sms_title = sms_title;
    }

    public String getSms_content() {
        return sms_content;
    }

    public void setSms_content(String sms_content) {
        this.sms_content = sms_content;
    }

    public String getSms_rec_obj() {
        return sms_rec_obj;
    }

    public void setSms_rec_obj(String sms_rec_obj) {
        this.sms_rec_obj = sms_rec_obj;
    }

    public Integer getSms_author() {
        return sms_author;
    }

    public void setSms_author(Integer sms_author) {
        this.sms_author = sms_author;
    }

    public Integer getSms_state() {
        return sms_state;
    }

    public void setSms_state(Integer sms_state) {
        this.sms_state = sms_state;
    }

    public Integer getMould_id() {
        return mould_id;
    }

    public void setMould_id(Integer mould_id) {
        this.mould_id = mould_id;
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

    public String getMould_content() {
        return mould_content;
    }

    public void setMould_content(String mould_content) {
        this.mould_content = mould_content;
    }

    public String getMould_fields() {
        return mould_fields;
    }

    public void setMould_fields(String mould_fields) {
        this.mould_fields = mould_fields;
    }

    public Integer getMun_id() {
        return mun_id;
    }

    public void setMun_id(Integer mun_id) {
        this.mun_id = mun_id;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}