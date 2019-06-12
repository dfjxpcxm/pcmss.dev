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
    private Integer  info_id;

	private String  index_id;

    /**
     *
     */
    private String  index_name;


	private String  index_value;
    /**
     *
     */
    private String  user_name;

    /**
    *
    */
    private Date  creat_time;
   /**
   *
   */
    private Date  update_time;
    
	// </editor-fold>



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

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Integer getInfo_id() {
		return info_id;
	}

	public void setInfo_id(Integer info_id) {
		this.info_id = info_id;
	}

	public String getIndex_id() {
		return index_id;
	}

	public void setIndex_id(String index_id) {
		this.index_id = index_id;
	}

	// <editor-fold defaultstate="collapsed" desc="成员Get/Set">
    
    // </editor-fold>
    
}