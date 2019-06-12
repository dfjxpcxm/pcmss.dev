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
package com.quick.portal.infoPush.catalogChange;

import java.util.Date;


/**
 * page数据对象
 */
public class CatalogChangeDO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
	
    // <editor-fold defaultstate="collapsed" desc="私有成员">

    /**
     *
     */

	private Integer  cid;
    private String  cata_id;
    /**
     *
     */
    private String  cata_name;
    /**
     *
     */
    private String  cata_value;

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

	public String getCata_name() {
		return cata_name;
	}

	public void setCata_name(String cata_name) {
		this.cata_name = cata_name;
	}

	public String getCata_value() {
		return cata_value;
	}

	public void setCata_value(String cata_value) {
		this.cata_value = cata_value;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCata_id() {
		return cata_id;
	}

	public void setCata_id(String cata_id) {
		this.cata_id = cata_id;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}


	// <editor-fold defaultstate="collapsed" desc="成员Get/Set">
    
    // </editor-fold>
    
}