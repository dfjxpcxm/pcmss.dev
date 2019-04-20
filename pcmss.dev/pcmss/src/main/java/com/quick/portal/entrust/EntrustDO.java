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
package com.quick.portal.entrust;

import java.util.Date;


/**
 * page数据对象
 */
public class EntrustDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
	
    // <editor-fold defaultstate="collapsed" desc="私有成员">
    /**
     *
     */
    private Integer  id;     	
    /**
     *
     */
    private String  from_user_id; 	
    /**
     *
     */
    private String  to_user_id;   	
    /**
     *
     */
    private String  menu_id;     	
    /**
     *
     */
    private Date  creat_time;    	
    /**
     *
     */
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFrom_user_id() {
		return from_user_id;
	}
	public void setFrom_user_id(String from_user_id) {
		this.from_user_id = from_user_id;
	}
	public String getTo_user_id() {
		return to_user_id;
	}
	public void setTo_user_id(String to_user_id) {
		this.to_user_id = to_user_id;
	}
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public Date getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(Date creat_time) {
		this.creat_time = creat_time;
	}
	@Override
	public String toString() {
		return "EntrustDO [id=" + id + ", from_user_id=" + from_user_id
				+ ", to_user_id=" + to_user_id + ", menu_id=" + menu_id
				+ ", creat_time=" + creat_time + "]";
	}
   
    // <editor-fold defaultstate="collapsed" desc="成员Get/Set">
    
    // </editor-fold>
    
    
}