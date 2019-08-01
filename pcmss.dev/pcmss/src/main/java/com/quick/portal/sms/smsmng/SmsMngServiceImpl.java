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
import com.quick.portal.sms.smsServices.SmsConstants;
import com.quick.portal.sms.smsServices.SmsMultiSender;
import com.quick.portal.sms.smsServices.SmsMultiSenderResult;
import com.quick.portal.sms.smsServices.SmsSingleSender;
import com.quick.portal.sms.smsServices.SmsSingleSenderResult;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
        BaseTable = "sms_info";
        BaseComment = "sms_info";
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
    public List<Map<String, Object>> getSmsSendData(String smsID) {
        return dao.getSmsSendData(smsID);
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
        List<SmsMngDO> phoneNUmList = null;
            if(null == entity.getFile_path() || "".equals(entity.getFile_path())){
                return ActionMsg.setError("上传文件路径为空");
            }

        /*Integer keyVal = entity.getSms_id();
        MouldMngDO mngDO = this.getTplInfo(entity.getMould_id(),entity.getMould_content());
        entity.setMould_num(mngDO.getMould_num());
        entity.setMould_id(mngDO.getMould_id());*/
        int c = 0;
        /*if (keyVal == null) {
            //失败：0；成功：1；发送中:2
            entity.setSms_state(2);

             //dao.insert(entity);
        } else {
            entity.setSms_state(2);
            //c = dao.update(entity);
            c = dao.update(entity);
        }*/
        entity.setSms_state(2);
        c = dao.insert(entity);
        //批量上传电话号码
 //       if(c == 1){
        if(c == 1 || c == 2){
            int id = getSmsInfoByTitle(entity.getSms_title());
            phoneNUmList = uploadPhoneNumInfo(entity,id);
       }
        Integer smsId = getSmsInfoByTitle(entity.getSms_title());
        boolean bool = smsSender(entity,phoneNUmList,smsId);
        smsLogMngService.saveSmsLogInfo(smsId,entity.getSms_title());
        if (c == 0) {
            return ActionMsg.setError("操作失败");
        }
        if(! bool){
            return ActionMsg.setError("短信发送接口失败");
        }
        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
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

    public List<String>  parseParamList(String params){
        List<String> pms = new ArrayList<>();
        if(params != null && !"".equals(params)){
            String [] parms = params.split(",");
            pms = Arrays.asList(parms);
        }
        return pms;
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
              List<String> pms = new ArrayList<>();
              smsSender = new SmsSingleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
              for(SmsMngDO m :phoneNUmList){
                  smsRes = m.getSms_rec_obj();
                  pms = parseParamList(m.getParams());
                  SmsSingleSenderResult singleReplyResult = smsSender.sendWithParam("", smsRes, entity.getMould_num(),
                          pms, sname,"","", SmsConstants.SENDSMS_URL);
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
        if(result == 0){
                entity.setSid(singleReplyResult.sid);
                entity.setSms_state(0);
                entity.setSms_id(id);
                dao.update(entity);

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
    public MouldMngDO getTplInfo(Integer mtype,String content){
        MouldMngDO mngDO = mouldMngService.getTplInfo(mtype,content);
        return mngDO;
    }

}