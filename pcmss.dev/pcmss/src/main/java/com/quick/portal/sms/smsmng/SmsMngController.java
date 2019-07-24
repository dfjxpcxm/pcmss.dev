/**
 * <h3>标题 : potal统一门户-sys_user </h3>
 * <h3>描述 : sys_user请求类</h3>
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
package com.quick.portal.sms.smsmng;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import com.quick.core.base.model.DataStore;
import com.quick.core.base.model.JsonDataGrid;
import com.quick.core.util.common.QCookie;
import com.quick.core.util.common.QRequest;
import com.quick.portal.search.infomng.FileUploadUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * sys_user请求类
 *
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/smsMng")
public class SmsMngController extends SysBaseController<SmsMngDO> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "smsMngService")
    private ISmsMngService smsMngService;
    private MultipartFile uploadFile;

    @Override
    public ISysBaseService<SmsMngDO> getBaseService() {
        return smsMngService;
    }

    @RequestMapping
    public String list(ModelMap model) {
        return view();
    }

    @RequestMapping
    public String edit(ModelMap model) {
        String userId = QCookie.getValue(request, "sbd.user_id");

        model.addAttribute("tpldir", TMPL_DOWNLOAD_PATH);

        model.addAttribute("sms_author", userId);

        return view();
    }

    @RequestMapping
    public String smslist(ModelMap model) {
        String smsId = request.getParameter("sms_id");
        model.addAttribute("sms_id", smsId);
        return view();
    }
    @RequestMapping
    public String smsedit(ModelMap model) {
        return view();
    }

    @RequestMapping(value = "/getSignNameData")
    @ResponseBody
    public Object getSignNameData() throws Exception {
        List<Map<String, Object>> list = smsMngService.getSignNameData();
        return new JsonDataGrid(list.size(), list).toObj();
    }

    @RequestMapping(value = "/getMouldNameData")
    @ResponseBody
    public Object getMouldNameData() throws Exception {
        List<Map<String, Object>> list = smsMngService.getMouldNameData();
        return new JsonDataGrid(list.size(), list).toObj();
    }
    @RequestMapping(value = "/save")
    @ResponseBody
    public DataStore saveAction(SmsMngDO model) {
        String filePath = FileUploadUtils.getImgUploadPath(request);
        model.setFile_path(filePath);
        return super.save(model);

    }

    /**
     * 获取发送短信详细信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getSmsSendData")
    @ResponseBody
    public Object getSmsSendData(String smsId) throws Exception {
        List<Map<String, Object>> list = smsMngService.getSmsSendData(smsId);
        return new JsonDataGrid(list.size(), list).toObj();
    }
    /**
     * 获取发送短信单个查看信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getSmsSendIdData")
    @ResponseBody
    public Object getSmsSendIData(String sendId) throws Exception {
        Map<String, Object> list = smsMngService.getSmsSendIdData(sendId);
        return list;
    }

    /**
     * 前端请求解析文件返回文件内容数量，即返回过滤之后的电话数量
     * @param uploadFile
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadFileAnaly")
    @ResponseBody
    public int uploadFileAnaly( @RequestParam("file_path") MultipartFile uploadFile) throws Exception {
        // 统计总数
        if (!uploadFile.isEmpty()) {
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                isr = new InputStreamReader(uploadFile.getInputStream());
                br = new BufferedReader(isr);
                String line = null;
                HashSet<List<String>> strs = new HashSet<List<String>>();
                while ((line = br.readLine()) != null) {
                    strs.add(Arrays.asList(line.split(",")[0]));//用于统计过滤重复的手机号！
                }
                //JSONArray array = toJsonArray(strs);//因为改为了HashSet无序集合，所以该方法不可用。需将HashSet改为List即可
                return strs.size()-1;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * 用于封装csv文件的数据
     * @param strs
     * @return
     */
    /*private JSONArray toJsonArray(List<List<String>> strs){
        JSONArray array = new JSONArray();
        for (int i = 1; i < strs.size(); i++) {
            JSONObject object = new JSONObject();
            for (int j = 0; j < strs.get(0).size(); j++) {
                object.put(strs.get(0).get(j),strs.get(i).get(j));
            }
            array.add(object);
        }
        return array;
    }*/
    private final static String TMPL_DOWNLOAD_PATH = "/upload/contentRule/smstpl20190720.cvs";


}