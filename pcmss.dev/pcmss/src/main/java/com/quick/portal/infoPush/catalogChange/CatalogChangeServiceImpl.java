/**
 * <h3>标题 : potal统一门户-page </h3>
 * <h3>描述 : page服务实现类</h3>
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quick.core.base.ISysBaseDao;
import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.DateTime;

/**
 * page服务实现类
 */
 @Transactional
 @Service("catalogChangeService")
public class CatalogChangeServiceImpl extends SysBaseService<CatalogChangeDO> implements ICatalogChangeService {
    
    /**
     * 构造函数
     */
    public CatalogChangeServiceImpl() {
        BaseTable = "page";
        BaseComment = "page";
        PrimaryKey = "page_id";
        NameKey = "page_name";
    }
    
    @Autowired
    private ICatalogChangeDao dao;

   
    
    @Override
    public ISysBaseDao getDao(){
        return dao;
    }
    

    /**
     * 删除业务
     * @param sysid
     * @return 
     */
    @Override
    public DataStore delete(String sysid) {
        dao.delete(sysid);
        return ActionMsg.setOk("操作成功");
    }


	@Override
	public List<Map<String, Object>> select() {
		// TODO Auto-generated method stub
		return dao.select();
	}


	@Override
	public int addInfo(List<Map<String, Object>> ls) {
		// TODO Auto-generated method stub
		CatalogChangeDO  pu = new CatalogChangeDO();
		int a = 0;
		Date now = DateTime.Now().getTime();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 0; i < ls.size(); i++){
			pu.setInfo_id((Integer) ls.get(i).get("info_id"));
			pu.setInfo_name((String) ls.get(i).get("info_name"));
			pu.setInfo_value((String) ls.get(i).get("info_value"));
			pu.setCreat_time( now);
			pu.setUpdate_time( now);
			a = a+dao.insert(pu);
		}
		return a;
	}
}