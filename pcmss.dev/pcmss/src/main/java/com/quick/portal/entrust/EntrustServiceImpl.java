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
package com.quick.portal.entrust;

import com.quick.core.base.ISysBaseDao;
import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * page服务实现类
 */
 @Transactional
 @Service("entrustService")
public class EntrustServiceImpl extends SysBaseService<EntrustDO> implements IEntrustService {
    
    /**
     * 构造函数
     */
    public EntrustServiceImpl() {
        BaseTable = "page";
        BaseComment = "page";
        PrimaryKey = "page_id";
        NameKey = "page_name";
    }
    
    @Autowired
    private IEntrustDao<EntrustDO> dao;

   
    
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


	/*@Override
	public List<Map<String, Object>> select(String user_name,String user_id) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_name",user_name);
		paramMap.put("user_id",user_id);
		return dao.selectUser(paramMap);
	}*/


	@Override
	public int addInfo(List<String> list,String user_id,String to_user_id) {
		// TODO Auto-generated method stub
		EntrustDO pu = new EntrustDO();
		int a = 0;
		Date now = DateTime.Now().getTime();
		deleteBy(user_id,to_user_id);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 0; i < list.size(); i++){
			pu.setFrom_user_id(user_id);
			pu.setTo_user_id(to_user_id);
			pu.setMenu_id((String)list.get(i));
			pu.setCreat_time(now);
			a = a+dao.insert(pu);
		}
		return a;
	}


	@Override
	public List<Map<String, Object>> selectFrom(String from_user_id,String to_user_id) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("from_user_id", from_user_id);
		paramMap.put("to_user_id", to_user_id);
		return dao.selectFrom(paramMap);
	}


	@Override
	public int deleteBy(String from_user_id, String to_user_id) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap = new HashMap<String, Object>();
		 paramMap.put("from_user_id", from_user_id);
   	  		paramMap.put("to_user_id", to_user_id);
		return dao.deleteBy(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectUserMenu(String user_id) {
		// TODO Auto-generated method stub
		/*Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("from_user_id", from_user_id);
		paramMap.put("to_user_id", to_user_id);*/
		return dao.selectUserMenu(user_id);
	}

	@Override
	public List<Map<String, Object>> selectNoEntrust() {
		// TODO Auto-generated method stub
		return dao.selectNoEntrust();
	}

	/*@Override
	public List<Map<String, Object>> selectInfo(String from_user_id, String to_user_id) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("from_user_id", from_user_id);
		paramMap.put("to_user_id", to_user_id);
		return dao.selectInfo(paramMap);
	}*/
}