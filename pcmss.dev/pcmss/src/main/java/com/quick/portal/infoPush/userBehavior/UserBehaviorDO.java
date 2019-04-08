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
package com.quick.portal.infoPush.userBehavior;

import java.util.Date;


/**
 * page数据对象
 */
public class UserBehaviorDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
	
    // <editor-fold defaultstate="collapsed" desc="私有成员">
    /**
     *
     */
    private Integer  id;     	
    /**
     *
     */
    private String  index_id; 	
    /**
     *
     */
    private String  index_name;   	
    /**
     *
     */
    private String  index_value;     	
    /**
     *
     */
    private String  pub_time;    	
    /**
     *
     */
    private String  appr_time;    	
	// </editor-fold>
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIndex_id() {
		return index_id;
	}
	public void setIndex_id(String index_id) {
		this.index_id = index_id;
	}
	public String getIndex_name() {
		return index_name;
	}
	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}
	public String getIndex_value() {
		return index_value;
	}
	public void setIndex_value(String index_value) {
		this.index_value = index_value;
	}
	public String getPub_time() {
		return pub_time;
	}
	public void setPub_time(String pub_time) {
		this.pub_time = pub_time;
	}
	public String getAppr_time() {
		return appr_time;
	}
	public void setAppr_time(String appr_time) {
		this.appr_time = appr_time;
	}
    
    // <editor-fold defaultstate="collapsed" desc="成员Get/Set">
    
    // </editor-fold>
    
}