package com.quick.portal.web.mainframe;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import com.quick.core.base.exception.ExceptionEnumServiceImpl;
import com.quick.core.util.common.CommonUtils;
import com.quick.core.util.common.JsonUtil;
import com.quick.core.util.common.QCookie;
import com.quick.portal.sysMenu.ISysMenuService;
import com.quick.portal.userAccessLog.IUserAccessLogService;
import com.quick.portal.userAccessLog.UserAccessLogConstants;
import com.quick.portal.userAccessLog.UserAccessLogServiceUtils;
import com.quick.portal.userRole.UserRoleDO;
import com.quick.portal.web.login.WebLoginConstants;
import com.quick.portal.web.login.WebLoginUser;
import com.quick.portal.web.model.DataResult;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.quick.portal.web.login.WebLoginUitls.isAdminRoleType;

/**
 * 查询菜单权限
 *
 * @author Administrator
 */
@Controller
@Scope("prototype")

public class MainFrameController extends SysBaseController<MainFrameBean> {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Resource(name = "mainFrameService")
    private MainFrameService mainFrameService;

    @Override
    public ISysBaseService getBaseService() {
        return mainFrameService;
    }

    @Resource(name = "userAccessLogService")
    private IUserAccessLogService userAccessLogService;

    @Resource(name = "sysMenuService")
    private ISysMenuService sysMenuService;

    /*
     * 查询菜单权限
     *
     */
    @RequestMapping(value = "/mainframe")
    public String goMainFrame(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String jsonStr = "false";
        //根据cookie拿到当前用户的id
        String userId = QCookie.getValue(request, "sbd.user_id");
        try {
            //权限菜单
            List<MainFrameBean> menuList = mainFrameService.searchMainFrame(userId);
            if (null != menuList && menuList.size() > 0) {
                MainFrameBean menuTree = this.convertListToTree(menuList);
                jsonStr = JsonUtil.toJson(menuTree.getChildren());
            } else {
                request.getSession().setAttribute("code", ExceptionEnumServiceImpl.USER_RESOURCE_NULL.getCode());
                request.getSession().setAttribute("message", ExceptionEnumServiceImpl.USER_RESOURCE_NULL.getMessage());
                return WebLoginConstants.REDIRECT_KEY.concat(WebLoginConstants.COMMON_ERROR_CONTROLLER);
            }
            model.addAttribute("data", jsonStr);
        } catch (Exception e) {
            request.getSession().setAttribute("code", ExceptionEnumServiceImpl.NO_PERMITION.getCode());
            request.getSession().setAttribute("message", ExceptionEnumServiceImpl.NO_PERMITION.getMessage() + "ERROR:=" + e.getMessage());
            return WebLoginConstants.REDIRECT_KEY.concat(WebLoginConstants.COMMON_ERROR_CONTROLLER);
        }
        return "page/index/mainframe";
    }


    /*
     * 菜单数据至拼装
     *
     */
    private MainFrameBean convertListToTree(List<MainFrameBean> menuList) {
        if (menuList == null || menuList.size() <= 0) {
            return new MainFrameBean();
        }
        MainFrameBean frameCol = null;
        int depth = 0;
        for (MainFrameBean mfrBean : menuList) {
            //二级及以下菜单
            if (mfrBean.getSuperMenuId() == 0) {
                frameCol = mfrBean;
                frameCol.setParent(true);
                frameCol.setDepth(depth);
                break;
            }
        }
        frameCol.buildChildren(menuList);
        return frameCol;
    }


    /*
     * 查询菜单权限
     * role_type:取最大值
     *
     */
    @RequestMapping(value = "/goIframe")
    public String goIframe(HttpServletRequest request, Model model) throws Exception {
        boolean isadmin = isAdminRoleType(loginer);
        List<UserRoleDO> listUser= loginer.getRoleList();
        int[] roleTypeId = new int[listUser.size()];
        int i = 0;
        for (UserRoleDO userRoleDO : listUser) {
            roleTypeId[i] = userRoleDO.getRole_type_id();
            i++;
        }
        Arrays.sort(roleTypeId);
        Map<String, Object> map = new HashMap<String, Object>();
        map =  mainFrameService.searchSysIndex(roleTypeId[roleTypeId.length-1]);
        String indexName = (String) map.get("index_name");
        return "page/index/" + indexName;
    }


    //添加用户
    @RequestMapping(value = "/sendLog")
    @ResponseBody
    public void sendLog(HttpServletRequest request,  HttpServletResponse res,int menuId, String menuNm) throws Exception {
        //记录日志
        res.setContentType("application/json; charset=utf-8");
        res.setCharacterEncoding("UTF-8");
        if(null != menuNm && !"".equals(menuNm)) {
            menuNm = java.net.URLDecoder.decode(menuNm, "UTF-8");
        }
        try {
            userAccessLogService.saveLog(request,
                    UserAccessLogConstants.SYS_LOG_TYPE_ID,
                    UserAccessLogConstants.OPER_MENU_USER_OP_TYPE,
                    menuId,
                    UserAccessLogConstants.OPER_MENU_USER_OP_DESC.concat(menuNm),
                    null,
                    "");
        } catch (Exception e) {
            throw new Exception("记录日志异常：" + e.getMessage());
        }

        //统一日志系统记录登录日志
        String flag = sysMenuService.getIsAppMenuByID(menuId);
        if(APP_FLAG.equals(flag)){
            loggerInfoLoginSystemLogInfo(request,loginer,menuNm);
        }

    }




    //APP:1;MENU:0
    @RequestMapping(value = "/getIsAppMenuByID")
    @ResponseBody
    public void getIsAppMenuByID(HttpServletRequest req, HttpServletResponse res, int menuId) throws Exception {
        res.setContentType("application/json; charset=utf-8");
        res.setCharacterEncoding("UTF-8");
        String flag = sysMenuService.getIsAppMenuByID(menuId);
        res.getWriter().write(flag);
    }


    @RequestMapping(value = "/getAcessData2Main")
    @ResponseBody
    public Object getAcessData2Main(String startDate,String endDate)throws Exception {
        DataResult dr = new DataResult();
        if(StringUtils.isEmpty(startDate)){
            dr.setError("系统统计不准确，开始时间异常：startDate="+startDate);
            return dr;
        }
        if(StringUtils.isEmpty(endDate)){
            dr.setError("系统统计不准确，开始时间异常：startDate="+endDate);
            return dr;
        }
        //T+1
        endDate = getPastDate(endDate,ONE_DAY);
        endDate = endDate.substring(0,10);
        Map<String, Object> p = new HashMap<>();
        p.put("startDate", startDate);
        p.put("endDate", endDate);
        AcessData2MainBean data = mainFrameService.getAcessData2Main(p);
        dr.setCode(1);
        dr.setData(data);
        return dr;
    }


    /*
          var data={
                "code": 200,
                "msg": null,
                "content": {
                    "triggerCountFailTotal": 418,
                    "triggerDayList": ["2019-09-24","2019-09-25","2019-09-26","2019-09-27","2019-09-28","2019-09-29","2019-09-30", "2019-10-01"],
                    "triggerCountSucTotal": 701,
                    "triggerDayCountSucList": [248, 230, 288, 311, 266, 308, 150, 170],
                }
            }
     */
    @RequestMapping(value = "/getMainData")
    @ResponseBody
    public Object getMainData(String startDate,String endDate) throws Exception {
        DataResult dr = new DataResult();
        if(StringUtils.isEmpty(startDate)){
            dr.setError("系统统计不准确，开始时间异常：startDate="+startDate);
            return dr;
        }
        if(StringUtils.isEmpty(endDate)){
            dr.setError("系统统计不准确，开始时间异常：startDate="+endDate);
            return dr;
        }
        //T+7
        startDate = getPastDate(endDate,WEEK_DAY);
        //T+1
        endDate = getPastDate(endDate,ONE_DAY);
        startDate = startDate.substring(0,10);
        endDate = endDate.substring(0,10);
        Map<String, Object> p = new HashMap<>();
        p.put("startDate", startDate);
        p.put("endDate", endDate);
        List<Map<String, Object>>  restList = new ArrayList<Map<String, Object>>();

        //查询作业调度数量
        JobDataBean jobData = mainFrameService.getJobData(p);

        //查询系统访问量
        List<Integer> loginCnt = mainFrameService.getLoginCntData(p);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("triggerCountFailTotal", jobData.getJob_fail_count());
        map.put("triggerCountSucTotal", jobData.getJob_suc_count());
        List<String>  days = getDays(startDate,endDate);
        map.put("triggerDayList", JsonUtil.serialize(days));
        map.put("triggerDayCountSucList", JsonUtil.serialize(loginCnt));
        restList.add(map);
        dr.setData(restList);
        dr.setCode(1);
        return dr;

    }




    public static String  getPastDate( String endDate ,int day)throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = df.parse(endDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, -day);  //减1天
        System.out.println(df.format(cal.getTime()));
        return df.format(cal.getTime());
    }

    public static List<String> getDays( String beginDate, String endDate)throws Exception{
        List<String> days = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(beginDate));
        for (long d = cal.getTimeInMillis(); d <= sdf.parse(endDate).getTime(); d = get_D_Plaus_1(cal)) {
            days.add(sdf.format(d));
        }
        return days;

    }

    public static long get_D_Plaus_1(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }


    /**
     *  在系统中记录门户系统用户登录的情况包括如下字段：
     *  （1）用户名称；（2）用户IP；（3）服务名称；（4）开始处理时间；（5）处理结果；
     *  （6）请求服务的URL。这些字段有（4）开始处理时间；（5）处理结果；（6）请求服务的URL不支持
     */
    public void loggerInfoLoginSystemLogInfo(HttpServletRequest request, WebLoginUser loginer,String menuNm){
        String ip = CommonUtils.getIpAddrAdvanced(request);
        String userName = loginer.getUser_name();
        String requestResult = "系统操作员:"+userName+"登录,".concat(menuNm);
        String operateType = menuNm+"->登录日志";
        String operatedUser = "系统操作员编号:"+loginer.getUser_id()+",系统操作员名称:"+loginer.getUser_name();
        String operLog = requestResult+",服务日志->登录日志";
        String serviceName = "服务名称:登录日志;服务方法名:";
        UserAccessLogServiceUtils.loggerLogInfo(logger,
                userName,operatedUser,operateType,requestResult,operLog,serviceName,ip);
    }


    public final static int WEEK_DAY = 7;
    public final static int ONE_DAY = 1;

    public final static String APP_FLAG = "1";

}
