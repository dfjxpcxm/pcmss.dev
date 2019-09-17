/**
 * <h3>标题 : potal统一门户-user_department </h3>
 * <h3>描述 : user_department服务实现类</h3>
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
package com.quick.portal.userDepartment;

import com.quick.core.base.ISysBaseDao;
import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.DateTime;
import com.quick.portal.ldapmng.IOrgLdapMngDao;
import com.quick.portal.security.authority.metric.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * user_department服务实现类
 */
 @Transactional
 @Service("userDepartmentService")
public class UserDepartmentServiceImpl extends SysBaseService<UserDepartmentDO> implements IUserDepartmentService {
    
    /**
     * 构造函数
     */
    public UserDepartmentServiceImpl() {
        BaseTable = "user_department";
        BaseComment = "部门";
        PrimaryKey = "dep_id";
        NameKey = "dep_name";
    }
    
    @Autowired
    private IUserDepartmentDao dao;

    @Resource(name = "orgLdapMngDao")
    private IOrgLdapMngDao orgLdapMngDao;
    
    @Override
    public ISysBaseDao getDao(){
        return dao;
    }
    
    /**
     * 保存业务
     * @return 
     */
    @Override
    public DataStore save(UserDepartmentDO entity) {
        //如果编号为空,新增实体对象,否则更新实体对象
        Integer val = entity.getDep_id();
        String valName = entity.getDep_name();
        Map map = new HashMap();
        map.put("dep_name",valName);
        List list = dao.select(map);
        int c = 0;
        Date now = DateTime.Now().getTime();
        String ldapState = PropertiesUtil.getPropery("ldap.auth.set");

            if(val == null || val == 0) {
                entity.setCre_time( now );  //新增时间
                entity.setUpd_time( now );  //修改时间
                if (list.size() == 0 || list == null){{
                    c = dao.insert(entity);
                    if("true".equals(ldapState)){
                        orgLdapMngDao.saveOrgLdapInfo(entity);
                    }
                }}

            }else {
                entity.setUpd_time( now );  //修改时间
                c = dao.update(entity);
                if("true".equals(ldapState)){
                    orgLdapMngDao.updateOrgLdapInfo(entity);
                }
            }
        if(c == 0)
            return ActionMsg.setError("操作失败");
        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }
    
    /**
     * 删除业务
     * @param sysid
     * @return 
     */
    @Override
    public DataStore delete(String sysid) {
        Map<String, Object> m = new HashMap<>();
        m.put("dep_id",Integer.parseInt(sysid));
        List<Map<String, Object>> retList = dao.select(m);
        Map<String, Object> mp = null;
        if(null != retList && !retList.isEmpty()){
           mp =retList.get(0);
        }
        String depId = mp.get("dep_global_id").toString();
        String ldapState = PropertiesUtil.getPropery("ldap.auth.set");
        if("true".equals(ldapState)){
            orgLdapMngDao.removeOrgLdapInfo(depId);
        }
        dao.delete(sysid);
        return ActionMsg.setOk("操作成功");
    }



    public DataStore syncOrgLdap(String orgids){
        int oid = Integer.valueOf(orgids);
        String ldapState = PropertiesUtil.getPropery("ldap.auth.set");
        if("true".equals(ldapState)){
            List<Map<String, Object>> retList = dao.getOrgInfoByIds(oid);
            int cnt = 0;
            if(null !=retList && !retList.isEmpty()){
                UserDepartmentDO depDO = null;
                for(Map<String, Object> mp:retList){
                    depDO = new UserDepartmentDO();
                    depDO.setDep_global_id(mp.get("dep_global_id").toString());
                    depDO.setDep_name(mp.get("dep_name")==null?"":mp.get("dep_name").toString());
                    depDO.setDep_state(mp.get("dep_state")==null?0:Integer.valueOf(mp.get("dep_state").toString()));
                    depDO.setSup_dep_global_id(mp.get("sup_dep_global_id")==null?"":mp.get("sup_dep_global_id").toString());
                    cnt = orgLdapMngDao.searchOrgLdapCnt(depDO);
                    if(cnt == 0){
                        orgLdapMngDao.saveOrgLdapInfo(depDO);
                    }else{
                        orgLdapMngDao.updateOrgLdapInfo(depDO);
                    }
                }
            }
        }

        return ActionMsg.setOk("同步LDAP用户成功");
    }


}