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
import com.quick.core.base.model.PageBounds;
import com.quick.core.util.common.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    //以下是roy编写 --2019-4-18 start
    //获取策略表信息 sys_res_conf_name
    @Override
    public List<Map<String,Object>> getResConfName() {
            return dao.getResConfName();
    }

    //获取策略表信息 sys_res_conf sys_menu
    @Override
    public List<Map<String, Object>> selectResInfo(Map<String, Object> map, PageBounds pager) {
        return dao.selectResInfo(map,pager);
    }

    @Override
    public List<Map<String, Object>> getResUserInfo(Map<String, Object> map, PageBounds pager) {
        return dao.getResUserInfo(map,pager);
    }

    /**
     * 联表查询 有用户信息的
     * @param sys_id
     * @return
     */
    @Override
    public Map<String, Object> getSysIdResInfo(String sys_id) {
        return dao.getSysIdResInfo(sys_id);
    }

    /**
     * 联表查询 没有用户信息的
     * @param sys_id
     * @return
     */
    @Override
    public Map<String, Object> getSysIdResUserInfo(String sys_id) {
        return dao.getSysIdResUserInfo(sys_id);
    }

    /**
     * 多人认证查询验证账号密码 sys_user
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

    /**
     * 多人认证配置查询是否开启sys_glabal_parm
     * @return
     */
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

    /**
     * 保存业务
     * @param entity
     * @return
     */
    @Override
    public DataStore saveRes(SysConfMngDO entity) {
        //如果编号为空,新增实体对象,否则更新实体对象
        Integer val = entity.getSys_id();
        int c = 0;
        Date now = DateTime.Now().getTime();
        //名称不能重复
      /*  if(exist("dep_name", entity.getDep_name(), val))
            return ActionMsg.setError("名称已存在，请换一个");*/
        if(val == null || val == 0) {
            entity.setCre_time( now );  //新增时间
           // entity.setUpd_time( now );  //修改时间

            c = dao.insertRes(entity);
        }else {
           // entity.setUpd_time( now );  //修改时间
            entity.setLentrust_set_prop(entity.getLentrust_set_prop()==null || "".equals(entity.getLentrust_set_prop()) || "0".equals(entity.getLentrust_set_prop())?"0":"1");
            entity.setLimt_res_set_prop(entity.getLimt_res_set_prop()==null || "".equals(entity.getLimt_res_set_prop()) || "0".equals(entity.getLimt_res_set_prop())?"0":"1");
            entity.setLimt_time_res_set_prop(entity.getLimt_time_res_set_prop()==null || "".equals(entity.getLimt_time_res_set_prop()) || "0".equals(entity.getLimt_time_res_set_prop())?"0":"1");
            entity.setLimt_user_res_set_prop(entity.getLimt_user_res_set_prop()==null || "".equals(entity.getLimt_user_res_set_prop()) || "0".equals(entity.getLimt_user_res_set_prop())?"0":"1");
            c = dao.updateRes(entity);
        }
        if(c == 0)
            return ActionMsg.setError("操作失败");
        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }


    /**
     * 删除业务sys_res_conf
     * @param sysid
     * @return
     */
    @Override
    public DataStore deleteRes(String sysid) {
        dao.deleteRes(sysid);
        return ActionMsg.setOk("操作成功");
    }

    /**
     * 查询资源菜单信息
     * @return
     */
    @Override
    public List<Map<String, Object>> selectUserInfo() {
        return dao.selectUserInfo();
    }
}