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
package com.quick.portal.sms.signmng;

import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.DateTime;
import com.quick.portal.appClass.AppClassDO;
import com.quick.portal.sms.smsServices.*;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
import com.quick.portal.sms.smssystem.SmsConstantsMicro;
import com.quick.portal.sms.smssystem.SmsSignHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * sys_sign_info服务实现类
 */
@Transactional
@Service("signMngService")
public class SignMngServiceImpl extends SysBaseService<SignMngDO> implements ISignMngService {

    @Resource(name = "smsLogMngService")
    private ISmsLogMngService smsLogMngService;

    public  SmsSignSender smsSign;
    //创建json解析工具类
    SmsSenderUtil util = new SmsSenderUtil();
    //创建调用短信签名添加httpclient
    SmsSignHttpClient smsSignHttpClient = new SmsSignHttpClient();
    /**
     * 构造函数
     */
    @Autowired
    public SignMngServiceImpl(ISignMngDao<SignMngDO> dao) {
        BaseTable = "sys_sign_info";
        BaseComment = "sys_sign_info";
        PrimaryKey = "sign_id";
        NameKey = "sign_id";
        smsSign = new SmsSignSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        setDao(dao);
        this.dao = dao;
    }

    private ISignMngDao<SignMngDO> dao;


    /**
     * 保存业务
     * @return
     */
    @Override
    public DataStore save(SignMngDO entity) {
        boolean bool = false;
        //如果编号为空,新增实体对象,否则更新实体对象
        Integer id = entity.getSign_id();
        int c = 0;
        if(id == null || id == 0) {
            //0：已通过, 1：待审核, 2：已拒绝
            entity.setSign_state(1);
            entity.setSign_num(0);
            c = dao.insert(entity);
            id = getSignInfoByName(entity.getSign_name());
            //调用创建签名接口
            bool = sendAddSignInfo(entity, id);
            smsLogMngService.saveSmsLogInfo(id,"新增短信签名："+"["+entity.getSign_name()+"]" );
        }else {
            entity.setSign_state(1);
            c = dao.update(entity);
            if(null != entity.getSign_num() && entity.getSign_num()>0){
                //调用修改签名接口
                bool = sendModSignInfo(entity, id);
                smsLogMngService.saveSmsLogInfo(id,"修改短信签名："+"["+entity.getSign_name()+"]" );
            }else{
                bool = sendAddSignInfo(entity, id);
                smsLogMngService.saveSmsLogInfo(id,"新增短信签名："+"["+entity.getSign_name()+"]" );
            }

        }
        if(c == 0) {
            return ActionMsg.setError("操作失败");
        }
        if(! bool) {
            return ActionMsg.setError("调用签名接口失败");
        }

        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }

    /**
     * 调用创建签名接口
     * @param entity
     * @param id
     * @return
     */
    public boolean sendAddSignInfo(SignMngDO entity,Integer id){
        boolean bool = false;
        try{

            //新的3.0api接口
            Object o = smsSignHttpClient.addSign("", entity.getSign_name(), entity.getApply_causes(), SmsConstantsMicro.ADD_SIGN_URL);
            JSONObject json = new JSONObject(String.valueOf(o));
            SmsSignReplyResult smsSignReplyResult = util.jsonToSmsSignReplyResult(json);
            //2.0api接口
            //SmsSignReplyResult signReplyResult = smsSign.sendSignInfo("", SmsConstants.INTERNAL_CODE, entity.getApply_causes(), entity.getSign_name(),0, SmsConstants.ADD_SIGN_URL);
            ParseSignReplyResult(smsSignReplyResult,id);
            bool = true;
        }catch (Exception e){
            System.out.println("调用创建签名接口失败！"+e.getLocalizedMessage());
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }


    /**
     * 调用修改签名接口
     * @param entity
     * @param id
     * @return
     */
    public boolean sendModSignInfo(SignMngDO entity,Integer id){
        boolean bool = false;
        try{
            //短信3.0api
            Object o = smsSignHttpClient.modSign("", entity.getSign_num(),entity.getSign_name(), entity.getApply_causes(), SmsConstantsMicro.MOD_SIGN_URL);
            JSONObject json = new JSONObject(String.valueOf(o));
            SmsSignReplyResult signReplyResult = util.jsonToSmsSignReplyResult(json);

            //短信2.0api
            //SmsSignReplyResult signReplyResult = smsSign.sendSignInfo("", SmsConstants.INTERNAL_CODE, entity.getApply_causes(), entity.getSign_name(),entity.getSign_num(), SmsConstants.MOD_SIGN_URL);

            ParseSignReplyResult(signReplyResult,id);
            bool = true;
        }catch (Exception e){
            System.out.println("调用修改签名接口失败！"+e.getLocalizedMessage());
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 通过签名名称查询签名信息
     */
    public Integer getSignInfoByName(String signName){
        Integer sid = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("sign_name",signName);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            sid = Integer.valueOf(mp.get("sign_id").toString());
        }
        return sid;
    }

    /**
     *
     * 解析签名返回内容
     *
     */
    public void ParseSignReplyResult (SmsSignReplyResult signReplyResult,int sid){
        int result = signReplyResult.result;
        SignMngDO entity  = new SignMngDO();
        if(result == 0){
            SmsSignReplyResult.data dt = (SmsSignReplyResult.data)signReplyResult.getData();
               entity = new SignMngDO();
                entity.setSign_num(dt.id);
                entity.setSign_name(dt.text);
                entity.setSign_state(dt.status);
                entity.setSign_id(sid);
                dao.update(entity);
        }else{
            entity.setSign_state(4);
            entity.setRemarks(signReplyResult.errmsg);
            entity.setSign_id(sid);
            dao.update(entity);
        }
    }


    @Override
    public List<Map<String, Object>> getSignTypeData() {
        return dao.getSignTypeData();
    }


    /**
     * 调用删除签名接口
     * @param sysid
     * @return
     */
    @Override
    public DataStore delete(String sysid) {
        //根据id去查询数据库中对应签名的编号
        Integer snum = getSignInfoByID(sysid);
        dao.delete(sysid);
        smsLogMngService.saveSmsLogInfo(Integer.valueOf(sysid),"删除短信签名："+"签名编号：" +sysid);
        /*ArrayList<Integer> signNums = new ArrayList<>();
        signNums.add(snum);*/
        //调用删除签名接口
        try{
            smsSignHttpClient.delSign(String.valueOf(snum),SmsConstantsMicro.DEL_SIGN_URL);
            //SmsRemoveReplyResult signReplyResult = smsSign.removeSignInfo(signNums, SmsConstants.DEL_SIGN_URL);
        }catch (Exception e){
            System.out.println("调用删除签名接口失败！"+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return ActionMsg.setOk("操作成功");
    }


    /**
     * 通过签名ID查询签名签名编号
     */
    public Integer getSignInfoByID(String sysid){
        Integer snum = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("sign_id",sysid);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            snum = Integer.valueOf(mp.get("sign_num").toString());
        }
        return snum;
    }


    /**
     * 通过签名ID查询签名名称
     */
    @Override
    public String getSignInfoById(Integer sid){
        String sname = "";
        Map<String, Object> map = new HashMap<>();
        map.put("sign_id",sid);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            sname = mp.get("sign_name").toString();
        }
        return sname;
    }

    /**
     * 从腾讯云短信服务器获取签名信息同步数据库
     * @return
     */
    @Override
    public DataStore syncSignInfo(Integer id) {

        ArrayList<Integer>  signIds = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if(null != id && id >0){
            //signIds.add(id);
            stringBuilder.append(id+",");
        }else{
            signIds = getSignIds();
            stringBuilder =  new StringBuilder();
            for (Integer sign:signIds){
                stringBuilder.append(sign+",");
            }

        }
        //将拼接的字符串转为string类型
        String signIdsAll = stringBuilder.toString();
        //删除最后一个字符  ,号
        String substring = signIdsAll.substring(0, signIdsAll.length() - 1);

        if(substring != null && substring.length() > 0){
            try{
                //3.0短信api接口
                Object o = smsSignHttpClient.getSign(substring,SmsConstantsMicro.GET_SIGN_URL);
                JSONObject json = new JSONObject(String.valueOf(o));
                SmsSignPullerReplyResult replyRest = util.jsonToSmsSignPullerReplyResult(json);
                //旧的2.0短信api接口
                //SmsSignPullerReplyResult replyRest = smsSign.getSignStatusPullerInfo(signIds,SmsConstants.GET_SIGN_URL);
                parseReplyResult(replyRest,id);
            }catch (Exception e){
                System.out.println("短信签名状态查询！"+e.getLocalizedMessage());
                e.printStackTrace();
                return ActionMsg.setOk("同步失败");
            }
            return ActionMsg.setOk("同步成功");
        }
        //短信签名状态查询
        return ActionMsg.setOk("无短信签名数据同步");
    }


    public ArrayList<Integer> getSignIds(){
        //查询拒绝待审核的签名信息
        List<Map<String, Object>> retList = dao.getSignStauteInfo();
        ArrayList<Integer>  signIds = new ArrayList<>();
        if(null != retList && retList.size()>0) {
            for (Map<String, Object> mp : retList) {
                signIds.add(Integer.valueOf(mp.get("sign_num").toString()));
            }
        }
        return signIds;
    }


    /**
     *
     * 解析签名返回内容
     *
     * {
     *     "result": 0,
     *     "errmsg": "",
     *     "count": 3,
     *     "data": [{
     *         "id": 123,
     *         "international": 0,
     *         "reply": "xxxxx",
     *         "status": 0,
     *         "text": "xxxxx",
     *         "apply_time": "2018-04-29 10:34:55",
     *         "reply_time": "2018-04-29 10:39:55"
     *     }]
     * }
     *
     */
    public void parseReplyResult (SmsSignPullerReplyResult signReplyResult,Integer id){
        int result = signReplyResult.result;
        Integer num = 0 ;
        SignMngDO entity = new SignMngDO();
        if(result == 0){
            ArrayList<SmsSignPullerReplyResult.data> datas = signReplyResult.datas;
            for(SmsSignPullerReplyResult.data dt : datas){
                entity = new SignMngDO();
                entity.setSign_num(dt.id);
                entity.setSign_name(dt.text);
                entity.setRemarks(dt.reply);
                entity.setSign_state(dt.status);
                num = getSignInfoByNum(dt.id);
                if(null == num  || num ==0){
                    entity.setSign_type(0);
                    entity.setSign_author(1);
                    entity.setApply_causes(dt.text);
                    dao.insert(entity);
                }else{
                    dao.updateReplyResult(entity);
                }

            }
        }
    }


    public Integer getSignInfoByNum(Integer sid){
        Integer sname = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("sign_num",sid);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            sname = Integer.valueOf(mp.get("sign_num").toString());
        }
        return sname;
    }
}