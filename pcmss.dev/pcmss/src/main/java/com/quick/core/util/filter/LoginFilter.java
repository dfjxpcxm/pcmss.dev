/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.quick.core.util.filter;

import com.quick.core.util.common.CommonUtils;
import com.quick.core.util.common.IpCommon;
import com.quick.core.util.common.QCommon;
import com.quick.core.util.common.QCookie;
import com.quick.portal.security.authority.metric.PropertiesUtil;
import com.quick.portal.security.sysconfmng.ISysConfMngService;
import com.quick.portal.security.sysconfmng.SysConfMngDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * loginCasFilter
 */
public class LoginFilter extends HttpServlet implements Filter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final static String COOKIE_USER_ID = "sbd.user_id";

    private final static ArrayList<String> prefixIgnores = new ArrayList<>();

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String user_id = QCookie.getValue(req, COOKIE_USER_ID);
        String url = req.getRequestURI();

        //白名单不认证
        boolean is_pass = isWhiteList(url, req);
        if (is_pass) {
            chain.doFilter(request, response);
            return;
        }

        if (user_id == null || "0".equals(user_id)) {
            logger.warn("Request for {} without valid user id, possibly because of session timeout.",
                    ((HttpServletRequest) request).getRequestURI());

            ((HttpServletRequest)request).getSession().invalidate();
            notifyMsgInvalid(res, req,TIMEOUT_MSG);
            return;
        }

        String authState = PropertiesUtil.getPropery("potal.auth.set");
        if("true".equals(authState)){
            //授权策略-支持对指定IP地址用户的访问授权策略控制；
            String ip = CommonUtils.getIpAddrAdvanced(req);
            boolean ipAuth  = isLimtIpAuthSet(ip);
            if(! ipAuth){
                notifyMsgInvalid(res, req,IPAUTH_MSG);
                return;
            }
            //授权策略-支持对LDAP标准属性的访问授权策略控制；
             boolean ldapAuth  = isLdapAuthSet(user_id);
             if(! ldapAuth){
                 notifyMsgInvalid(res, req,LDAPAUTH_MSG);
                 return;
             }

            //系统提供短信发送量的预警功能，用户可以设置相应短信的发送量限额，
            // 超出限额自定提醒给管理员，防止盗刷等事件发生。
            boolean msgAuth  = isMsgAuthSet();
            if(! msgAuth){
                notifyMsgLimtSet(res, req,getNotifyMsg("" ,MSCAUTH_MSG));
//
            }


             //授权策略-支持对同一权限可以分配的次数的授权策略控制；
            List<Map<String,Object>> confList  = searchLimtResLoginSet();
            String resids = getLimtResIDBySysConf(confList);
            if(null != resids && !"".equals(resids)){
                notifyMsgLimtSet(res, req,getNotifyMsg(resids,LIMT_RES_LOGIN_MSG));
            }

            //授权策略-支持某一时间间隔内一个特定权限可以被访问的次数的授权策略控制；
            List<Map<String,Object>> limtTimeList  = searchLimtTimeLoginSet();
            String tmeResids = getLimtResIDBySysConf(limtTimeList);
            if(null != tmeResids && !"".equals(tmeResids)){
                notifyMsgLimtSet(res, req,getNotifyMsg(tmeResids,LIMT_TIME_LOGIN_MSG));
            }

            //授权策略-支持一个用户在一时间间隔内可以访问权限资源的次数的授权策略控制；
            List<Map<String,Object>> limtUserList  = searchLimtUserTimeLoginSet(user_id);
            String uResids = getLimtResIDBySysConf(limtUserList);
            if(null != uResids && !"".equals(uResids)){
                notifyMsgLimtSet(res, req,getNotifyMsg(uResids ,LIMT_USER_TIME_LOGIN_MSG));
            }
        }


        chain.doFilter(request, response);
    }



    /*
     * 返回资源ID字符串
     */
    public String getLimtResIDBySysConf(List<Map<String,Object>> retList){
        String resids = "";
        for(Map<String,Object> mps :retList){
            String limtResLogin = mps.get("isLimtRes").toString();
            if(null !=limtResLogin &&"false".equals(limtResLogin)){
                resids  += mps.get("resId").toString().concat(",");
            }
        }

        resids = resids.lastIndexOf(",")>=0 ? resids.substring(0,resids.lastIndexOf(",")) :resids;
        return  resids;
    }



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext sc = filterConfig.getServletContext();
        XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
        if(cxt != null && cxt.getBean("sysConfMngService") != null && sysConfMngService == null){
            sysConfMngService = (ISysConfMngService) cxt.getBean("sysConfMngService");
         }

        String cp = filterConfig.getServletContext().getContextPath();
        String ignoresParam = filterConfig.getInitParameter("prefixIgnore");
        String[] ignoreArray = ignoresParam.split(",");
        for (String s : ignoreArray) {
            prefixIgnores.add(cp + s);
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    public boolean isWhiteList(String url, HttpServletRequest request) {
        String contextPath = request.getContextPath();
        if (url.equalsIgnoreCase(contextPath + "/"))
            return true;

        for (String prefix : prefixIgnores) {
            if (url.startsWith(prefix))
                return true;
        }

        return false;
    }



    /*
     * 授权策略-支持对指定IP地址用户的访问授权策略控制；
     * isLdapAuthSet:返回true,可以访问授权策略控制；否则，提示无法访问系统（ldapMask值不是secret）
     *
     */
    public boolean isLimtIpAuthSet(String ip) {
        boolean bool = true;
        //查询系统参数表IP配置授权策略 parm_title in('ip_set','st_ip_val','ed_ip_val')
        List<Map<String,Object>> retList = sysConfMngService.getLimtIpSystemParmInfo();
        Map<String,Object> mp = new HashMap<>();
        if(null != retList && retList.size()>0){
            mp = retList.get(0);
            String ldapState = mp.get("parm_val").toString();
            if("true".equals(ldapState)){
                mp = retList.get(1);
                String stIP = mp.get("parm_val").toString();
                mp = retList.get(2);
                String edIP = mp.get("parm_val").toString();
                /*
                 *当前用户客户端IP,必须stIP-edIP中间可以访问授权策略控制，
                 *否则，提示无法访问系统
                 */
                boolean isIP = IpCommon.isValidRange(stIP,edIP,ip);
                if(isIP){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return bool;
    }

    /*
     * 授权策略-支持对LDAP标准属性的访问授权策略控制；
     * isLdapAuthSet:返回true,可以访问授权策略控制；否则，提示无法访问系统（ldapMask值不是secret）
     *
     */
    public boolean isLdapAuthSet(String userId) {
            boolean bool = true;
            //查询系统参数表LDAP配置授权策略 parm_title in('ldap_set','ldap_mask')
            List<Map<String,Object>> retList = sysConfMngService.getLimtLdapSystemParmInfo();
            Map<String,Object> mp = new HashMap<>();
            if(null != retList && retList.size()>0){
                mp = retList.get(0);
                String ldapState = mp.get("parm_val").toString();
                if("true".equals(ldapState)){
                    mp = retList.get(1);
                    String ldapMask = mp.get("parm_val").toString();
                    /*
                     *LDAP标准属性的访问授权策略控制：ldapMask值是secret且用户LDAP属性ldapProperty（secret）
                     * 提示无法访问系统，
                     *否则，可以访问授权策略控制
                     */
                    String ldapProperty =sysConfMngService.getUserLdapProPertyByUserID(userId);
                    if(null != ldapProperty && ldapProperty.equals(ldapMask)){
                        return false;
                    }else{
                        return true;
                    }
                }
            }
        return bool;
    }


    /*
       系统提供短信发送量的预警功能，用户可以设置相应短信的发送量限额，
        超出限额自定提醒给管理员，防止盗刷等事件发生。
     */
    public boolean isMsgAuthSet() {
        boolean bool = true;
        //系统提供短信发送量的预警功能，用户可以设置相应短信的发送量限额， parm_title in ('msg_set','msg_val')
        List<Map<String,Object>> retList = sysConfMngService.getLimtMsgSystemParmInfo();
        Map<String,Object> mp = new HashMap<>();
        if(null != retList && retList.size()>0){
            mp = retList.get(0);
            String msgState = mp.get("parm_val").toString();
            if("true".equals(msgState)){
                mp = retList.get(1);
                String msgCnt = mp.get("parm_val").toString();
                /*
                 *系统提供短信发送量的预警功能，用户可以设置相应短信的发送量限额，
                 *   超出限额自定提醒给管理员，防止盗刷等事件发生。
                 *   短信的发送量与系统短信限额比较，发送量超出，提示无法访问系统，
                 *否则，可以访问授权策略控制
                 */
               boolean isBool = sysConfMngService.getUserMsgDataCnt(msgCnt);
               return isBool;
            }
        }
        return bool;
    }

    /*
     *授权策略-支持对同一权限可以分配的次数的授权策略控制；
     */
    public  List<Map<String,Object>> searchLimtResLoginSet() {
        //查询系统参数表对同一权限可以分配的次数的授权策略控制 -查询设置资源开关信息： limt_res_set='1'
        List<SysConfMngDO> retList = sysConfMngService.getLimtResLoginSystemParmInfo();
        List<Map<String,Object>> mapList = new ArrayList<>();
        Map<String,Object> dataMap = new HashMap<>();
        if(null != retList && retList.size()>0){
            Map<String,Object> sysParms = null;
            for(SysConfMngDO sysConf :retList){
                /*
                 *查询系统参数表某一个特定权限配置授权策略：
                 * 某一个特定资源访问次数小于系统参数设置值
                 * 可以访问授权策略控制，
                 *否则，提示无法访问系统
                 *
                 */
                /*
                   SELECT count(1) FROM user_access_log  WHERE  menu_id = 1
                 */
                sysParms = new HashMap<>();
                sysParms.put("resId",sysConf.getRes_id());
                int cnt = sysConfMngService.getLimtResLoginInfo(sysParms);
                Map<String,Object> confMap = getLimtResStateInfo(sysConf.getCnt().intValue(),cnt,sysConf.getRes_id().toString());
                mapList.add(confMap);
            }

        }
        return mapList;
    }

    /*
     * 授权策略-支持某一时间间隔内一个特定权限可以被访问的次数的授权策略控制；
     * isLdapAuthSet:返回true,可以访问授权策略控制；
     * 否则，提示无法访问系统（ldapMask值不是secret）
     *
     */

    public List<Map<String,Object>> searchLimtTimeLoginSet() {
        //查询系统参数表某一时间间隔内一个特定权限配置授权策略 limt_time_res_set_prop ='1'
        List<SysConfMngDO> retList = sysConfMngService.getLimtTimeLoginSystemParmInfo();
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(null != retList && retList.size()>0){
            Map<String,Object> sysParms = null;
            for(SysConfMngDO sysConf :retList){
                sysParms = new HashMap<>();
                sysParms.put("resId",sysConf.getRes_id());
                sysParms.put("timeout",sysConf.getTimeout());
                /*
                 *查询系统参数表某一时间间隔内一个特定权限配置授权策略：
                 * 某一时间间隔内一个特定资源访问次数小于系统参数设置值
                 * 可以访问授权策略控制，
                 *否则，提示无法访问系统
                 *
                 */
                /*
                   SELECT count(1) FROM user_access_log
                    WHERE  menu_id = 1 AND log_time> DATE_SUB( NOW( ), INTERVAL 3600 SECOND )
                 */
                int cnt = sysConfMngService.getLimtTimeLoginInfo(sysParms);
                Map<String,Object> confMap = getLimtResStateInfo(sysConf.getCnt().intValue(),cnt,sysConf.getRes_id().toString());
                mapList.add(confMap);
            }
        }
        return mapList;
    }


    public  Map<String,Object> getLimtResStateInfo(int fixCnt,int logCnt,String resId){
        Map<String,Object> confMap = new HashMap<>();
        if(logCnt < fixCnt){
            confMap.put("isLimtRes","true");
            confMap.put("resId",resId);
        }else{
            confMap.put("isLimtRes","false");
            confMap.put("resId",resId);
        }
        return confMap;
    }

    /*
     * 授权策略-支持一个用户在一时间间隔内可以访问权限资源的次数的授权策略控制；
     * isLdapAuthSet:返回true,可以访问授权策略控制；否则，提示无法访问系统（ldapMask值不是secret）
     *
     */

    public List<Map<String,Object>> searchLimtUserTimeLoginSet(String uid) {
        //查询系统参数表支持一个用户在一时间间隔内可以访问权限资源的次数的授权策略控制 parm_title in('limt_user_res_set','limt_user_id','limt_user_res_id','limt_user_timeout','limt_user_cnt')
        List<SysConfMngDO> retList = sysConfMngService.getLimtUserTimeLoginSystemParmInfo();
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(null != retList && retList.size()>0){
            Map<String,Object> sysParms = null;
            for(SysConfMngDO sysConf :retList){
                sysParms = new HashMap<>();
                sysParms.put("resId",sysConf.getRes_id());
                sysParms.put("timeout",sysConf.getTimeout());
                sysParms.put("userId",sysConf.getUser_id());
                /*
                 *一个用户在一时间间隔内可以访问权限资源的次数授权策略控制：
                 * 某一时间间隔内一个特定资源访问次数小于系统参数设置值
                 * 可以访问授权策略控制，
                 *否则，提示无法访问系统
                 */
                  /*
                   SELECT count(1) FROM user_access_log
                    WHERE  menu_id = 1 AND log_time> DATE_SUB( NOW( ), INTERVAL 3600 SECOND ) and user_id=1
                 */
                int cnt = sysConfMngService.getLimtUserTimeLogin(sysParms);
                Map<String,Object> confMap = getLimtResStateInfo(sysConf.getCnt().intValue(),cnt,sysConf.getRes_id().toString());
                mapList.add(confMap);
            }
        }
        return mapList;
    }



    private void notifyMsgInvalid(HttpServletResponse response, HttpServletRequest request,String msg) {
        String host = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath()
                + "/";
        if (isAjax(request)) {
            writeJs("{\"code\":-99,\"msg\":\""+msg+"\", \"url\":\"" + host + "\"}", response);
        } else {
            String casUrl = PropertiesUtil.getPropery("sso.cas.server.prefixUrl");
            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath()
                    + "/";
            String retUrl = casUrl.concat("/logout?service=").concat(QCommon.urlEncode(url));
            writeMsg(msg, host, retUrl, request, response,"");
        }
    }


    private void notifyMsgLimtSet(HttpServletResponse response, HttpServletRequest request,String msg) {
        String host = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath()
                + "/";
            String retUrl = "";
            writeMsg(msg, host, retUrl, request, response,"1");

    }

    private boolean isAjax(HttpServletRequest request) {
        //如果是ajax请求响应头会有x-requested-with
        return request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
    }

    private void writeJs(String msg, HttpServletResponse response) {
        response.setContentType("application/json; charset=utf-8"); // 输出JS文件
        response.setCharacterEncoding("UTF-8");
        try {
            OutputStream out = response.getOutputStream();
            out.write(msg.getBytes("UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeMsg(String msg, String host, String url, HttpServletRequest request, HttpServletResponse response,String flg) {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String outString = "<html><body><script src=\"" + host + "res/plugin/jQuery/jquery-1.11.3.min.js\" type=\"text/javascript\"></script>";
        outString += "<link href=\"" + host + "res/layer/skin/default/layer.css\" rel=\"stylesheet\">";
        outString += "<script src=\"" + host + "res/layer/layer.js\"></script>";
        if(null != flg && "1".equals(flg)){
            outString += "<script language=javascript>layer.alert('" + msg + "');</script>";
        }else{
            outString += "<script language=javascript>layer.alert('" + msg + "',function(){(window.parent||window).location='"
                    + url + "';});</script>";
        }

        outString += "</body></html>";
        try {
            response.getWriter().print(outString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public String getNotifyMsg(String resids ,String msg){
        //修改资源ID状态禁用
        if(null != resids && !"".equals(resids)){
            sysConfMngService.updResStateInfoByID(resids);
        }
        String notifyMsg = msg.replaceAll("res",resids);
        return notifyMsg;
    }

    private ISysConfMngService sysConfMngService;

    public final  static  String TIMEOUT_MSG ="会话已超时，请重新登录！";

    public final  static  String LDAPAUTH_MSG ="系统设置指定LDAP用户属性访问限制，请联系管理员重新设置LDAP属性！";


    public final  static  String MSCAUTH_MSG ="系统设置短信的发送量已经超出限额，请联系管理员重新设置短信的发送量限额！";




    public final  static  String IPAUTH_MSG ="系统设置指定IP访问限制，请联系管理员重新设置IP段！";


    public final  static  String LIMT_RES_LOGIN_MSG ="用户访问res资源超越次数，请重新设置用户访问res资源次数！";


    public final  static  String LIMT_TIME_LOGIN_MSG ="指定时间间隔,用户访问res资源是超越次数，请重新设置用户访问res资源次数！！";


    public final  static  String LIMT_USER_TIME_LOGIN_MSG ="指定用户特定时间间隔,用户访问res资源超越次数，请重新设置用户访问res资源次数！";
}