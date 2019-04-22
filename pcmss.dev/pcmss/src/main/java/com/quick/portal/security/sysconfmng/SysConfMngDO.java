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
package com.quick.portal.security.sysconfmng;

import java.util.Date;


/**
 * sys_glabal_parm数据对象
 */
public class SysConfMngDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;

    public SysConfMngDO(){}

    public SysConfMngDO(SysConfMngDO u){
        setSys_id(u.getSys_id());
        setParm_title(u.getParm_title());
        setParm_val(u.getParm_val());
        setTimeout(u.getTimeout());
        setUser_id(u.getUser_id());
        setCnt(u.getCnt());
        setRes_id(u.getRes_id());
        setLimt_res_set_prop(u.getLimt_res_set_prop());
        setLimt_time_res_set_prop(u.getLimt_time_res_set_prop());
        setLimt_user_res_set_prop(u.getLimt_user_res_set_prop());
        setLentrust_set_prop(u.getLentrust_set_prop());
        setIds(u.getIds());
    }
    // <editor-fold defaultstate="collapsed" desc="私有成员">
    /**
     *用户ID
     */
    private Integer  sys_id;
    /**
     *用户名
     */
    private String  parm_title;
    /**
     *用户口令
     */
    private String  parm_val;
  /**
     * 中文名
     */
    private String parm_chinese;
    /**
     *upd_time
     */
    private Date  cre_time;


    private Integer  res_id;//资源ID
    private Integer  timeout;//超时定义
    private Integer  user_id;//用户ID
    private Integer  cnt;//次数

    //设置资源开关
    private String  limt_res_set_prop;
    //设置时间资源开关
    private String  limt_time_res_set_prop;
    //设置时间资源开关
    private String  limt_user_res_set_prop;
    //设置委托开关
    private String  lentrust_set_prop;

    private String ids;

    private Integer sys_res_id;

    private String sys_res_name;

    public Integer getSys_id() {
        return sys_id;
    }

    public void setSys_id(Integer sys_id) {
        this.sys_id = sys_id;
    }

    public String getParm_title() {
        return parm_title;
    }

	 public String getParm_chinese() {
        return parm_chinese;
    }

    public void setParm_chinese(String parm_chinese) {
        this.parm_chinese = parm_chinese;
    }

    public void setParm_title(String parm_title) {
        this.parm_title = parm_title;
    }

    public String getParm_val() {
        return parm_val;
    }

    public void setParm_val(String parm_val) {
        this.parm_val = parm_val;
    }

    public Date getCre_time() {
        return cre_time;
    }

    public void setCre_time(Date cre_time) {
        this.cre_time = cre_time;
    }

    public Integer getRes_id() {
        return res_id;
    }

    public void setRes_id(Integer res_id) {
        this.res_id = res_id;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getLimt_res_set_prop() {
        return limt_res_set_prop;
    }

    public void setLimt_res_set_prop(String limt_res_set_prop) {
        this.limt_res_set_prop = limt_res_set_prop;
    }

    public String getLimt_time_res_set_prop() {
        return limt_time_res_set_prop;
    }

    public void setLimt_time_res_set_prop(String limt_time_res_set_prop) {
        this.limt_time_res_set_prop = limt_time_res_set_prop;
    }

    public String getLimt_user_res_set_prop() {
        return limt_user_res_set_prop;
    }

    public void setLimt_user_res_set_prop(String limt_user_res_set_prop) {
        this.limt_user_res_set_prop = limt_user_res_set_prop;
    }

    public String getLentrust_set_prop() {
        return lentrust_set_prop;
    }

    public void setLentrust_set_prop(String lentrust_set_prop) {
        this.lentrust_set_prop = lentrust_set_prop;
    }


    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getSys_res_id() {
        return sys_res_id;
    }

    public void setSys_res_id(Integer sys_res_id) {
        this.sys_res_id = sys_res_id;
    }

    public String getSys_res_name() {
        return sys_res_name;
    }

    public void setSys_res_name(String sys_res_name) {
        this.sys_res_name = sys_res_name;
    }
}