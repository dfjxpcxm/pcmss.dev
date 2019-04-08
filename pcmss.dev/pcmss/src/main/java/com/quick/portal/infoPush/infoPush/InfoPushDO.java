/**
 * <h3>标题 : potal统一门户-page </h3>
 * <h3>描述 : page数据对象</h3>
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
package com.quick.portal.infoPush.infoPush;

import java.util.Date;


/**
 * page数据对象
 */
public class InfoPushDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
	
    // <editor-fold defaultstate="collapsed" desc="私有成员">
    /**
     *
     */
    private Integer  id;     	
    /**
     *
     */
    private Integer  push_id; 	
    /**
     *
     */
    private Integer  push_type;   	
    /**
     *
     */
    private String  push_name;     	
    /**
     *
     */
    private String  push_info;    	
    /**
     *
     */
    private Integer  info_state; 
    /**
    *
    */
    private Date  creat_time;
   /**
   *
   */
    private Date  update_time;
    
	// </editor-fold>
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPush_id() {
		return push_id;
	}
	public void setPush_id(Integer push_id) {
		this.push_id = push_id;
	}
	public Integer getPush_type() {
		return push_type;
	}
	public void setPush_type(Integer push_type) {
		this.push_type = push_type;
	}
	public String getPush_name() {
		return push_name;
	}
	public void setPush_name(String push_name) {
		this.push_name = push_name;
	}
	public String getPush_info() {
		return push_info;
	}
	public void setPush_info(String push_info) {
		this.push_info = push_info;
	}
	public Integer getInfo_state() {
		return info_state;
	}
	public void setInfo_state(Integer info_state) {
		this.info_state = info_state;
	}
	public Date getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(Date creat_time) {
		this.creat_time = creat_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

    // <editor-fold defaultstate="collapsed" desc="成员Get/Set">
    
    // </editor-fold>
    
}