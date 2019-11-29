package com.seaboxdata.portal.mobile;

import com.quick.core.base.SysApiController;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.MD5Util;
import com.quick.portal.sysUser.ISysUserDao;
import com.quick.portal.sysUser.ISysUserService;
import com.quick.portal.sysUser.SysUserDO;
import com.quick.portal.web.model.DataResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.provider.MD5;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.quick.core.util.common.MD5Util.encode;

/**
 * Created by GaoPh on 2018/7/9.
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/mobile/sysUser")
public class UserApiController extends SysApiController {
    @Resource(name = "sysUserService")
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserDao<SysUserDO> iSysUserDao;
    //App修改密码
    @RequestMapping(value = "/modifyPwdByApp")
    @ResponseBody
    public DataResult modifyPwdByApp(String user_id,String user_old_pw, String new_password, HttpServletResponse res)  {
        Map<String,Object> map = new HashMap<>();
        DataResult result = null;
        if(null == user_old_pw || "".equals(user_old_pw)){
        	 result = new DataResult(0,"您输入旧密码不能为空，请重新输入密码！");
             return result;
        }
        if(null == new_password || "".equals(new_password)){
        	 result = new DataResult(0,"您输入新密码不能为空，请重新输入密码！");
             return result;
        }

        map.put("user_password",user_old_pw);
        map.put("user_name",user_id);

        List<Map<String,Object>> list = sysUserService.select(map);
        if(null == list || list.size()==0){
              result = new DataResult(0,"您输入用户或密码不准确，请重新输入密码！");
              return result;
        }
    	SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUser_password(new_password);
        sysUserDO.setUser_name(user_id);
        try{
        	 iSysUserDao.modifyPwdByApp(sysUserDO);
        }catch(Exception e){
        	  result = new DataResult(0,"修改密码失败，请重新操作！");
              return result;
        }
        result = new DataResult(0,"OK"); 
        return result;
    }

    //App密码修改--旧密码校验
    @RequestMapping(value="checkAppOldPw")
    @ResponseBody
    public DataResult checkAppOldPw(HttpServletResponse res,Integer user_id, String user_old_pw) throws IOException {
        Map<String,Object> map = new HashMap<>();
        DataResult result = new DataResult();
        map.put("user_password",user_old_pw);
        map.put("user_id",user_id);
        List<Map<String,Object>> list = sysUserService.select(map);
        if(list.size()!=0){
            result.setCode(1);
            result.setMsg("OK");
            result.setVersion("1.0");
        }else {
            result.setCode(0);
            result.setMsg("输入的密码有错，请重新输入！");
            result.setVersion("1.0");
        }
        return result;
    }


    //App修改密码
    @RequestMapping(value = "/getUserPwdData")
    @ResponseBody
    public DataResult getUserPwdData(String userId, String userPwd, String encType, String sysSource) throws NoSuchAlgorithmException {
        DataResult result = new DataResult();
        if(userPwd.length() != 32){
            result.setCode(3);
            result.setMsg("密码长度不正确！");
            return result;
        }

        Map<String,Object> mp = new HashMap<>();
        mp.put("user_name",userId);
        SysUserDO sysUserDO = new SysUserDO();
        List<SysUserDO> ListsysUserInfo = iSysUserDao.getUserInfo(mp);
        if(null != ListsysUserInfo &&ListsysUserInfo.size() >0 ){
            sysUserDO.setUser_password(userPwd);//加密
            sysUserDO.setUser_name(userId);
            sysUserDO.setUser_cert_type(encType);
            sysUserDO.setUser_global_id(sysSource);
            int c = sysUserService.updateUserPassword(sysUserDO);
            if(c == 1 ){
                result.setCode(0);
                result.setMsg("OK");
            }else{
                result.setCode(1);
                result.setMsg("修改失败");
            }

        }else {
            result.setCode(2);
            result.setMsg("用户不存在！用户名:"+userId);
        }
        return result;
    }

}
