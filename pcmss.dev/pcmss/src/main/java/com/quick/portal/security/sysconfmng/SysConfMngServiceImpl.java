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
package com.quick.portal.security.sysconfmng;

import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.quick.core.util.common.MD5Util.encode;


/**
 * sys_glabal_parm服务实现类
 */
@Transactional
@Service("sysConfMngService")
public class SysConfMngServiceImpl extends SysBaseService<SysConfMngDO> implements ISysConfMngService {

    /**
     * 构造函数
     */
    @Autowired
    public SysConfMngServiceImpl(ISysConfMngDao<SysConfMngDO> dao) {
        BaseTable = "sys_glabal_parm";
        BaseComment = "sys_glabal_parm";
        PrimaryKey = "sys_id";
        NameKey = "sys_id";

        setDao(dao);
        this.dao = dao;
    }

    private ISysConfMngDao<SysConfMngDO> dao;


    @Override
    public List<Map<String, Object>> getLimtIpSystemParmInfo() {
        return dao.getLimtIpSystemParmInfo();
    }

    @Override
    public List<Map<String, Object>> getLimtLdapSystemParmInfo() {
        return dao.getLimtLdapSystemParmInfo();
    }

    @Override
    public String getUserLdapProPertyByUserID(String uid) {
        String ldapProperty = "";
        int id  = Integer.valueOf(uid);
        List<Map<String, Object>> retList = dao.getUserLdapProPertyByUserID(id);
        if(null != retList && !retList.isEmpty()){
            Map<String, Object> mp = retList.get(0);
            ldapProperty = mp.get("ldap_prop").toString();
        }
        return  ldapProperty;
    }

    @Override
    public  List<SysConfMngDO> getLimtResLoginSystemParmInfo() {
        return dao.getLimtResLoginSystemParmInfo();
    }

    @Override
    public int getLimtResLoginInfo(Map<String, Object> mp) {
        return dao.getLimtResLoginInfo(mp);
    }

    @Override
    public List<SysConfMngDO> getLimtTimeLoginSystemParmInfo() {
        return dao.getLimtTimeLoginSystemParmInfo();
    }

    @Override
    public int getLimtTimeLoginInfo(Map<String, Object> mp) {
        return dao.getLimtTimeLoginInfo(mp);
    }

    @Override
    public List<SysConfMngDO> getLimtUserTimeLoginSystemParmInfo() {
        return dao.getLimtUserTimeLoginSystemParmInfo();
    }

    @Override
    public int getLimtUserTimeLogin(Map<String, Object> mp) {
        return dao.getLimtUserTimeLogin(mp);
    }

    @Override
    public void updResStateInfoByID(String id) {
         dao.updResStateInfoByID(id);
    }
	 /**
     * 多人认证
     * @param
     * @param
     * @return
     */
    @Override
    public DataStore peopleAuthor(String user_name, String user_password) {
        HashMap m = new HashMap();
        if(user_password == null){
            m.put("user_name",user_name);
            List<Map<String, Object>> retList = dao.getUserName(m);
            if(null !=retList && !retList.isEmpty()){
                return ActionMsg.setOk("查询成功");
            }
        }else
        {

            m.put("user_name",user_name);
            m.put("user_password",encode(user_password));
            List<Map<String, Object>> retList = dao.getUserInfoPwd(m);
            if(null !=retList && !retList.isEmpty()){
                return ActionMsg.setOk("查询成功");
            }
        }
        return ActionMsg.setOk("查询失败");
    }

    @Override
    public DataStore manyPeopleCertification() {
        boolean man_wit_set = false;
        boolean wit_val = false;
        List<SysConfMngDO> li = dao.manyPeopleCertification();
        for (int i = 0; i < li.size(); i++) {
            if (li.get(i).getParm_title().equals("many_wit_set") && li.get(i).getParm_val().equals("true")){
                man_wit_set = true;
            }
            if (li.get(i).getParm_title().equals("wit_val") && li.get(i).getParm_val().equals("3")){
                wit_val = true;

            }


        }
        if (man_wit_set == true && wit_val == true){
            return ActionMsg.setOk("已开启");
        }
        return ActionMsg.setOk("未开启");
    }
}