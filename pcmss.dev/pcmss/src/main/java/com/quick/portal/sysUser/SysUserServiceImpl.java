/**
 * <h3>标题 : potal统一门户-sys_user </h3>
 * <h3>描述 : sys_user服务实现类</h3>
 * <h3>日期 : 2018-04-13</h3>
 * <h3>版权 : Copyright (C) 北京东方金信科技有限公司</h3>
 *
 * <p>
 *
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
package com.quick.portal.sysUser;

import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.portal.ldapmng.IUserLdapMngDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * sys_user服务实现类
 */
@Transactional
@Service("sysUserService")
public class SysUserServiceImpl extends SysBaseService<SysUserDO> implements ISysUserService {

    /**
     * 构造函数
     */
    @Autowired
    public SysUserServiceImpl(ISysUserDao<SysUserDO> dao) {
        BaseTable = "sys_user";
        BaseComment = "sys_user";
        PrimaryKey = "user_id";
        NameKey = "user_name";

        setDao(dao);
        this.dao = dao;
    }

    private ISysUserDao<SysUserDO> dao;


    @Resource(name = "userLdapMngDao")
    private IUserLdapMngDao userLdapMngDao;

    @Override
    public DataStore save(SysUserDO sysUserDO) {
        if (sysUserDO.getUser_password() == null || sysUserDO.getUser_password().equals(""))
            sysUserDO.setUser_password("21232f297a57a5a743894a0e4a801fc3");
        sysUserDO.setRoles(String.join(",", sysUserDO.getRole_ids()));
        Integer val = sysUserDO.getUser_id();

        if(val == null || val == 0) {
            userLdapMngDao.saveUserLdapInfo(sysUserDO);
        }else{
            userLdapMngDao.updateUserLdapInfo(sysUserDO);
        }
        return super.save(sysUserDO);
    }

    /**
     * 删除业务
     * @param user_id
     * @return
     */
    @Override
    public DataStore delete(String user_id) {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUser_id(Integer.valueOf(user_id));
        Map<String, Object> m = new HashMap<>();
        m.put("user_id",sysUserDO.getUser_id());
        List<SysUserDO> retList = dao.getUserInfo(m);
        SysUserDO sysVO = null;
        if(null != retList && !retList.isEmpty()){
            sysVO = retList.get(0);
        }
        userLdapMngDao.removeUserLdapInfo(sysVO.getUser_name());
        dao.delete(sysUserDO);
        if (sysUserDO.getError_no() == 1)
            return ActionMsg.setOk("操作成功");
        else
            return ActionMsg.setError("操作失败");
    }


    /*
     * 锁定用户帐号
     */
    @Override
    public void updateUserStatueByUersId(String userId) {
        dao.updateUserStatueByUersId(userId);

    }


    /*
     * 查询指定IP,密码错误次数
     */
    @Override
    public Map<String, Object> getLockCount(String ip) {
        // TODO Auto-generated method stub
        List<Map<String, Object>> retList = dao.getLockCount(ip);
        if (null != retList && retList.size() > 0) {
            return retList.get(0);
        }
        return null;
    }

    /*
     *  通过用户名称查询用户信息
     * (non-Javadoc)
     * @see com.quick.portal.sysUser.ISysUserService#isExitUserInfoByUserId(java.lang.String)
     */
    @Override
    public Map<String, Object> isExitUserInfoByUserId(String userId) {
        List<Map<String, Object>> retList = dao.isExitUserInfoByUserId(userId);
        if (null != retList && retList.size() > 0) {
            return retList.get(0);
        }
        return null;
    }

    public List<SysUserDO> getUserInfo(Map<String, Object> m) {
        List<SysUserDO> retList = dao.getUserInfo(m);
        if (null != retList && retList.size() > 0) {
            return retList;
        }
        return null;
    }

    public DataStore updatePassword(SysUserDO sysUserDO){
        int c = dao.updatePassword(sysUserDO);
        if (c == 1)
            return ActionMsg.setOk("密码修改成功");
        else
            return ActionMsg.setError("密码修改失败");
    }



    public DataStore syncUserLdap(String userid){
        int uid  = Integer.valueOf(userid);
        List<Map<String, Object>> retList = dao.getUserInfoByIds(uid);
        if(null !=retList && !retList.isEmpty()){
            SysUserDO sysUserDO = null;
            for(Map<String, Object> mp:retList){
                sysUserDO = new SysUserDO();
                sysUserDO.setUser_real_name(mp.get("user_real_name").toString());
                sysUserDO.setUser_name(mp.get("user_real_name").toString());
                sysUserDO.setUser_email(mp.get("user_email").toString());
                sysUserDO.setUser_state(Integer.valueOf(mp.get("user_state").toString()));
                sysUserDO.setUser_password(mp.get("user_password").toString());
                sysUserDO.setUser_tel(mp.get("user_tel").toString());
                userLdapMngDao.saveUserLdapInfo(sysUserDO);
            }
        }
        return ActionMsg.setOk("同步LDAP用户成功");
    }

}