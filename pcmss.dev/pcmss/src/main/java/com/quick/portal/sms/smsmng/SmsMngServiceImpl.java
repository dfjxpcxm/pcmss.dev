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
package com.quick.portal.sms.smsmng;

import com.csvreader.CsvReader;
import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.portal.sms.mouldmng.IMouldMngService;
import com.quick.portal.sms.mouldmng.MouldMngDO;
import com.quick.portal.sms.signmng.ISignMngService;
import com.quick.portal.sms.smsServices.*;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
import com.quick.portal.sms.smssystem.SmsConstantsMicro;
import com.quick.portal.sms.smssystem.SmsSendHttpClient;
import com.quick.portal.sms.smssystem.SmsSignHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * sys_glabal_parm服务实现类
 */
@Transactional
@Service("smsMngService")
public class SmsMngServiceImpl extends SysBaseService<SmsMngDO> implements ISmsMngService {

    public SmsSingleSender smsSender;

    public SmsMultiSender  smsMultiSender;
    //创建json解析工具类
    SmsSenderUtil util = new SmsSenderUtil();
    //创建调用短信签名添加httpclient
    SmsSendHttpClient smsSendHttpClient = new SmsSendHttpClient();

    @Resource(name = "signMngService")
    private ISignMngService signMngService;

    @Resource(name = "mouldMngService")
    private IMouldMngService mouldMngService;



    @Resource(name = "smsLogMngService")
    private ISmsLogMngService smsLogMngService;

    /**
     * 构造函数
     */
    @Autowired
    public SmsMngServiceImpl(ISmsMngDao<SmsMngDO> dao) {
        BaseTable = "sys_sms_task";
        BaseComment = "sys_sms_task";
        PrimaryKey = "sms_id";
        NameKey = "sms_id";

        setDao(dao);
        this.dao = dao;
    }

    private ISmsMngDao<SmsMngDO> dao;


    @Override
    public List<Map<String, Object>> getSignNameData() {
        return dao.getSignNameData();
    }

    @Override
    public List<Map<String, Object>> getMouldNameData() {
        return dao.getMouldNameData();
    }

    @Override
    public List<Map<String, Object>> getSmsSendData(SmsMngDO sms) {
        return dao.getSmsSendData(sms);
    }

    @Override
    public Map<String, Object> getSmsSendIdData(String sendId) {
        return dao.getSmsSendIdData(sendId);
    }

    private Class<SmsMngDO> entityClass;


    /**
     *   批量上传电话号码
     * @param entity
     * @param id
     */
    public  List<SmsMngDO> uploadPhoneNumInfo(SmsMngDO entity, int id) {
        String filePath = entity.getFile_path();
        SmsMngDO smsMngDO = null;
        List<SmsMngDO> phoneNUmList = new ArrayList<SmsMngDO>();
        CsvReader reader = null;
        //List<String[]> dataList = new ArrayList<String[]>();
        //正则表达式判断参数
        String reg="\\{+[0-9]+}";
        try {
            reader = new CsvReader(filePath,',',Charset.forName("GBK"));
            reader.readHeaders();
           // String[] headArray = reader.getHeaders();//获取标题
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = null;
            //System.out.println(headArray[0] + headArray[1] + headArray[2]);
            // 逐条读取记录，直至读完
            while (reader.readRecord()) {
                String moudle_content=entity.getMould_content();
                matcher = pattern.matcher(moudle_content);
                // 读一整行
                //System.out.println(reader.getRawRecord());
                // 读这行的第一列
                //System.out.println(reader.get("客户手机号"));
                // 读这行的第二列
                // System.out.println(reader.get(1));
                int i = 1;
                String params = "";
                while(matcher.find()){
                    moudle_content = moudle_content.replace(matcher.group(),reader.get(i));
                    params += reader.get(i).concat("#");
                    i++;
                }
                smsMngDO = new SmsMngDO();
                smsMngDO.setSms_rec_obj(reader.get(0));
                smsMngDO.setSms_content(moudle_content);
                smsMngDO.setSms_id(id);
                smsMngDO.setSms_state(entity.getSms_state());
                smsMngDO.setSid(entity.getSid());
                String param = "";
                if(params.endsWith("#")){
                    param = params.substring(0,params.length()-1);
                }else{
                    param = params;
                }
                smsMngDO.setParams(param);
                phoneNUmList.add(smsMngDO);
            }
            dao.insertPhoneNUmList(phoneNUmList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                reader.close();
            }
        }
        return phoneNUmList;
    }

    @Override
    public DataStore save(SmsMngDO entity) {
        //文件导入
        if(null == entity.getFile_path() || "".equals(entity.getFile_path())){
            return ActionMsg.setError("上传文件路径为空");
        }
        MouldMngDO mngDO = this.getTplInfo(entity.getMould_id());
        entity.setMould_num(mngDO.getMould_num());
        entity.setSms_state(2);
        Integer c = dao.insert(entity);
        int id = getSmsInfoByTitle(entity.getSms_title());
        //批量上传电话号码
        List<SmsMngDO> phoneNUmList = uploadPhoneNumInfo(entity,id);
        boolean bool = smsSender(entity,phoneNUmList,id);
        List<Integer> sendIds = this.getSendInfoById(id);
        for(Integer sid :sendIds){
            smsLogMngService.saveSmsLogInfo(sid,entity.getSms_title());
        }

        if (c == 0) {
            return ActionMsg.setError("操作失败");
        }
        if(! bool){
            return ActionMsg.setError("短信发送接口失败");
        }
        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }



    public List<Integer> getSendInfoById(int id){
        List<Integer> sendIds = new ArrayList<>();
        List<Map<String, Object>> retList = dao.getSendInfoById(id);
        if(null != retList &&retList.size()>0){
            for(Map<String, Object> mp:retList){
                sendIds.add(Integer.valueOf(mp.get("send_id").toString()));
            }
        }
       return sendIds;

    }
    /**
     * 通过短信标题查询短信信息
     */
    public Integer getSmsInfoByTitle(String smsTitle){
        Integer smsId = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("sms_title",smsTitle);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            smsId = Integer.valueOf(mp.get("sms_id").toString());
        }
        return smsId;
    }

    //返回短信内容参数
    public String  parseParamList(String params){
        //ArrayList<String> pms = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        /*if(params != null && !"".equals(params)){
            String [] parms = params.split("#");
            for(String pm : parms){
                pms.add(pm);
            }
        }*/
        String [] parms = params.split("#");
        for(String pm : parms){
            stringBuilder.append(pm + ",");
        }

        String paramssub = stringBuilder.toString();

        return paramssub.substring(0,paramssub.length()-1);
    }


    /**
     * 短信发送
     * @param entity
     * @param phoneNUmList
     * @return
     */
    public boolean smsSender(SmsMngDO entity,List<SmsMngDO> phoneNUmList,Integer keyVal){
        boolean bool = false;
        String sname = this.getSignName(entity.getSign_id());
        try{
              String smsRes = "";
//              List<String> pms = new ArrayList<>();
            String pms = "";
              //smsSender = new SmsSingleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
              for(SmsMngDO m :phoneNUmList){
                  smsRes = m.getSms_rec_obj();
                  pms = parseParamList(m.getParams());

                  //短信3.0api发送接口
                  Object o = smsSendHttpClient.sendData(pms, sname, smsRes, entity.getMould_num(), SmsConstantsMicro.SENDSMS_URL);
                  JSONObject json = new JSONObject(String.valueOf(o));
                  SmsSingleSenderResult singleReplyResult = util.jsonToSmsSingleSenderResult(json);
                  /*SmsSingleSenderResult singleReplyResult = smsSender.sendWithParam("", smsRes, entity.getMould_num(),
                          pms, sname,"","", SmsConstants.SENDSMS_URL);*/
                  parseSingleReplyResult(singleReplyResult,keyVal);
                  bool = true;
              }
        }catch (Exception e){
            System.out.println("调用短信发送接口失败！"+e.getLocalizedMessage());
            return false;
        }
        return  bool;
    }


    public ArrayList<String> parseNumbersList(List<SmsMngDO> phoneNUmList){
        ArrayList<String> phoneNumbers = new ArrayList<>();
        for(SmsMngDO sms:phoneNUmList){
            phoneNumbers.add(sms.getSms_rec_obj());
        }
        return phoneNumbers;
    }



    /**
     *
     * {
     *     "result": 0,
     *     "errmsg": "OK",
     *     "ext": "",
     *     "fee": 1,
     *     "sid": "xxxxxxx"
     * }
     * 解析签名返回内容
     *
     */
    public void parseSingleReplyResult (SmsSingleSenderResult singleReplyResult,int id){
        int result = singleReplyResult.result;
        SmsMngDO entity = new SmsMngDO();
        if("send success".equals(singleReplyResult.errmsg) && result == 0){
                entity.setSid(singleReplyResult.sid);
                entity.setSms_state(0);
                entity.setSms_id(id);
                dao.update(entity);
                dao.updateInfo(entity);
        }else{
            entity.setSms_state(4);
            entity.setRemarks(singleReplyResult.errmsg);
            entity.setSms_id(id);
            dao.update(entity);
        }

    }
    /**
     * {
     *     "result": 0,
     *     "errmsg": "OK",
     *     "ext": "",
     *     "detail": [
     *         {
     *             "errmsg": "OK",
     *             "fee": 1,
     *             "mobile": "13788888888",
     *             "nationcode": "86",
     *             "result": 0,
     *             "sid": "xxxxxxx"
     *         },
     *         {
     *             "errmsg": "OK",
     *             "fee": 1,
     *             "mobile": "13788888889",
     *             "nationcode": "86",
     *             "result": 0,
     *             "sid": "xxxxxxx"
     *         }
     *     ]
     * }
     * parseSmsReplyResult
     */

    public void parseSmsReplyResult (SmsMultiSenderResult smsReplyResult, int sid){
        int result = smsReplyResult.result;
        SmsMngDO entity  = new SmsMngDO();
        if(result == 0){
            ArrayList<SmsMultiSenderResult.Detail> datas = smsReplyResult.details;
            for(SmsMultiSenderResult.Detail dt : datas){
                entity = new SmsMngDO();
                entity.setSid(dt.sid);
                entity.setSms_state(dt.result);
                entity.setSms_id(sid);
                entity.setSms_rec_obj(dt.phoneNumber);
                entity.setRemarks(smsReplyResult.errmsg);
                entity.setFee(dt.fee);
                dao.insertSmsReplyResult(entity);
            }
        }else{
            entity.setSms_state(4);
            entity.setRemarks(smsReplyResult.errmsg);
            entity.setSms_id(sid);
            dao.insertSmsReplyResult(entity);
        }
    }

    //通过签名ID查询签名名称
    public String getSignName(Integer sid){
        String sname = signMngService.getSignInfoById(sid);
        return sname;
    }



    //通过签名ID查询签名名称
    public MouldMngDO getTplInfo(Integer id){
        MouldMngDO mngDO = mouldMngService.getTplInfo(id);
        return mngDO;
    }

}