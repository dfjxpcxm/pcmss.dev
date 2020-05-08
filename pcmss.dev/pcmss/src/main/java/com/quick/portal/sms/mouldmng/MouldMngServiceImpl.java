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
package com.quick.portal.sms.mouldmng;

import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.portal.sms.smsServices.*;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
import com.quick.portal.sms.smsmng.SmsMngDO;
import com.quick.portal.sms.smssystem.SmsConstantsMicro;
import com.quick.portal.sms.smssystem.SmsSignHttpClient;
import com.quick.portal.sms.smssystem.SmsTemplateHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * sys_mould_info服务实现类
 */
@Transactional
@Service("mouldMngService")
public class MouldMngServiceImpl extends SysBaseService<MouldMngDO> implements IMouldMngService {

    @Resource(name = "smsLogMngService")
    private ISmsLogMngService smsLogMngService;

    public  SmsTempleSender smsTemple;

    //创建json解析工具类
    SmsSenderUtil util = new SmsSenderUtil();

    //创建调用短信签名添加httpclient
    SmsTemplateHttpClient smsTemplateHttpClient = new SmsTemplateHttpClient();



    /**
     * 构造函数
     */
    @Autowired
    public MouldMngServiceImpl(IMouldMngDao<MouldMngDO> dao) {
        BaseTable = "sys_mould_info";
        BaseComment = "sys_mould_info";
        PrimaryKey = "mould_id";
        NameKey = "mould_id";
        smsTemple = new SmsTempleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        setDao(dao);
        this.dao = dao;
    }

    private IMouldMngDao<MouldMngDO> dao;


    @Override
    public List<Map<String, Object>> getMouldTypeData() {
        return dao.getMouldTypeData();
    }



    private Class<SmsMngDO> entityClass;


   /**
    *
    * 调用模板接口
    */
    @Override
    public DataStore save(MouldMngDO entity) {
        boolean bool = false;
        //如果编号为空,新增实体对象,否则更新实体对象
        Integer keyVal = entity.getMould_id();

        int c = 0;
        if (keyVal == null || keyVal == 0) {
            entity.setMould_state(1);
            entity.setMould_num(0);
            c = dao.insert(entity);
            keyVal = getTplInfoByName(entity.getMould_name());
            //调用创建模板接口
            bool =  sendAddTplInfo(entity,keyVal);
            smsLogMngService.saveSmsLogInfo(keyVal,"新增短信模板："+"["+entity.getMould_name()+"]" );
        } else {
            c = dao.update(entity);
            if(null != entity.getMould_num() && entity.getMould_num()>0){
                //调用修改模板接口
                bool =  sendModTplInfo(entity,keyVal);
                smsLogMngService.saveSmsLogInfo(keyVal,"修改短信模板："+"["+entity.getMould_name()+"]" );
            }else{
                bool =  sendAddTplInfo(entity,keyVal);
                smsLogMngService.saveSmsLogInfo(keyVal,"新增短信模板："+"["+entity.getMould_name()+"]" );
            }
        }
        if (c == 0) {
            return ActionMsg.setError("操作失败");
        }
        if (!bool) {
            return ActionMsg.setError("调用模板接口失败");
        }
        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }

    /**
     * 调用创建模板接口
     * @param entity
     * @param id
     * @return
     */
    public boolean sendAddTplInfo(MouldMngDO entity,Integer id){
        boolean bool = false;
        try{
            //3.0短信api接口
            Object o = smsTemplateHttpClient.addTemplate(entity.getMould_name(),entity.getMould_content(),entity.getMould_name(),entity.getMould_type(), SmsConstantsMicro.ADD_TEMPLATE_URL);
            JSONObject json = new JSONObject(String.valueOf(o));
            SmsTempleReplyResult tplReplyResult = util.jsonToSmsTmplReplyResult(json);

           // SmsTempleReplyResult tplReplyResult = smsTemple.sendTempleInfo("", SmsConstants.INTERNAL_CODE, entity.getMould_content(), entity.getMould_name(),0, entity.getMould_type(),SmsConstants.ADD_TEMPLATE_URL);

            parseSmsTempleReplyResult(tplReplyResult,id);
            bool = true;
        }catch (Exception e){
            System.out.println("调用创建模板接口失败！"+e.getLocalizedMessage());
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }
    /**
     * 调用修改模板接口
     * @param entity
     * @param id
     * @return
     */
    public boolean sendModTplInfo(MouldMngDO entity,Integer id){
        boolean bool = false;
        try{

            //3.0短信api接口
            Object o = smsTemplateHttpClient.modTemplate(entity.getMould_name(),entity.getMould_num(),entity.getMould_content(),entity.getMould_name(),entity.getMould_type(), SmsConstantsMicro.ADD_TEMPLATE_URL);
            JSONObject json = new JSONObject(String.valueOf(o));
            SmsTempleReplyResult tplReplyResult = util.jsonToSmsTmplReplyResult(json);

            //SmsTempleReplyResult tplReplyResult = smsTemple.sendTempleInfo("", SmsConstants.INTERNAL_CODE, entity.getMould_content(), entity.getMould_name(),entity.getMould_num(), entity.getMould_type(),SmsConstants.MOD_TEMPLATE_URL);
            parseSmsTempleReplyResult(tplReplyResult,id);
            bool = true;
        }catch (Exception e){
            System.out.println("调用修改模板接口失败！"+e.getLocalizedMessage());
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }


    /**
     * 通过模板名称查询模板信息
     */
    public Integer getTplInfoByName(String tplName){
        Integer sid = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("mould_name",tplName);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            sid = Integer.valueOf(mp.get("mould_id").toString());
        }
        return sid;
    }


    /**
     *
     * 解析签名返回内容
     *{
     *     "result": 0,
     *     "errmsg": "",
     *     "data": {
     *         "id": 123,
     *         "international": 0,
     *         "status": 1,
     *         "text": "xxxxx",
     *         "type": 0
     *     }
     * }
     *
     */
    public void parseSmsTempleReplyResult (SmsTempleReplyResult tplReplyResult, int sid){
        int result = tplReplyResult.result;
        MouldMngDO entity  = new MouldMngDO();
        if(result == 0){
            SmsTempleReplyResult.data dt = (SmsTempleReplyResult.data)tplReplyResult.getData();
                entity = new MouldMngDO();
                entity.setMould_num(dt.id);
                entity.setMould_state(dt.status);
                entity.setMould_content(dt.text);
                entity.setMould_type(dt.type);
                entity.setMould_id(sid);
                dao.update(entity);
        }else{
            entity.setMould_state(4);
            entity.setRemarks(tplReplyResult.errmsg);
            entity.setMould_id(sid);
            dao.update(entity);
        }
    }



    /**
     * 调用删除模板接口
     * @param sysid
     * @return
     */
    @Override
    public DataStore delete(String sysid) {
        Integer snum = getTplInfoByID(sysid);
        dao.delete(sysid);
        smsLogMngService.saveSmsLogInfo(Integer.valueOf(sysid),"删除短信模板："+"模板编号：" +sysid);
       /* ArrayList<Integer> tplNums = new ArrayList<>();
        tplNums.add(snum);*/
        //调用删除签名接口
        try{
            smsTemplateHttpClient.delTemplate(String.valueOf(snum), SmsConstantsMicro.DEL_TEMPLATE_URL);
            //SmsRemoveReplyResult tplReplyResult = smsTemple.removeTempleInfo(tplNums, SmsConstants.DEL_TEMPLATE_URL);
        }catch (Exception e){
            System.out.println("调用删除模板接口失败！"+e.getLocalizedMessage());
            return null;
        }
        return ActionMsg.setOk("操作成功");
    }

    /**
     * 通过签名ID查询签名签名编号
     */
    public Integer getTplInfoByID(String sysid){
        Integer snum = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("mould_id",sysid);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            snum = Integer.valueOf(mp.get("mould_num").toString());
        }
        return snum;
    }

    /**
     * 获取模板编号
     */
    @Override
    public MouldMngDO getTplInfo(Integer id){
        Map<String, Object> map = new HashMap<>();
        map.put("mould_id",id);
        List<Map<String, Object>> retList = dao.select(map);
        MouldMngDO entity = new MouldMngDO();
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            entity.setMould_id(Integer.valueOf(mp.get("mould_id").toString()));
            entity.setMould_num(Integer.valueOf(mp.get("mould_num").toString()));
        }
        return entity;
    }

    @Override
    public List<Map<String, Object>> getMouldTypeContent(Map<String, Object> queryMap) {
        return dao.getMouldTypeContent(queryMap);
    }

    @Override
    public DataStore syncMouldInfo(Integer id) {

        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Integer>  tplIds = new ArrayList<>();
        if(null != id && id >0){
            //tplIds.add(id);
            stringBuilder.append(id+",");
        }else{
            tplIds = getMouldIds();
            stringBuilder =  new StringBuilder();
            for (Integer tplId:tplIds){
                stringBuilder.append(tplId+",");
            }
        }

        //将拼接的字符串转为string类型
        String signIdsAll = stringBuilder.toString();
        //删除最后一个字符  ,号
        String substring = signIdsAll.substring(0, signIdsAll.length() - 1);

        if(substring != null && substring.length() > 0){
            //短信模板状态查询
            try{
                //短信3.0api
                Object o = smsTemplateHttpClient.getTemplateInfoById(substring, SmsConstantsMicro.GET_TEMPLATE_URL);
                JSONObject json = new JSONObject(String.valueOf(o));
                SmsTemplePullerReplyResult replyRest = util.jsonToTemplePullerReplyResult(json);

                //SmsTemplePullerReplyResult replyRest = smsTemple.getTempleStatusPullerInfoByTplId(tplIds,SmsConstants.GET_TEMPLATE_URL);
                parseReplyResult(replyRest,id);
            }catch (Exception e){
                System.out.println("短信模板状态查询！"+e.getLocalizedMessage());
                e.printStackTrace();
                return ActionMsg.setOk("同步失败");
            }
            return ActionMsg.setOk("同步成功");
        }
        return ActionMsg.setOk("无短信签名数据同步");
    }


        public ArrayList<Integer> getMouldIds() {
            //查询拒绝待审核的签名信息
            List<Map<String, Object>> retList = dao.getMouldStauteInfo();
            ArrayList<Integer> tplIds = new ArrayList<>();
            if (null != retList && retList.size() > 0) {
                for (Map<String, Object> mp : retList) {
                    tplIds.add(Integer.valueOf(mp.get("mould_num").toString()));
                }
            }
            return tplIds;
        }

    /**
     *
     * 解析签名返回内容
     *{
     *     "result": 0,
     *     "errmsg": "",
     *     "total": 10,
     *     "count": 1,
     *     "data": [
     *         {
     *             "id": 123,
     *             "international": 0,
     *             "reply": "xxxxx",
     *             "status": 0,
     *             "text": "xxxxx",
     *             "type": 0,
     *             "title": "xxxxx",
     *             "apply_time": "xxxxx",
     *             "reply_time": "xxxxx"
     *         }
     *     ]
     * }
     * }
     *
     */
    public void parseReplyResult(SmsTemplePullerReplyResult tplReplyResult,Integer id){
        int result = tplReplyResult.result;
        Integer mnum = 0;
        MouldMngDO entity  = new MouldMngDO();
        if(result == 0){
            ArrayList<SmsTemplePullerReplyResult.data> datas = tplReplyResult.datas;
            for(SmsTemplePullerReplyResult.data dt : datas){
                entity = new MouldMngDO();
                entity.setMould_num(dt.id);
                entity.setRemarks(dt.reply);
                entity.setMould_state(dt.status);
                entity.setMould_content(dao.getSmsContent(dt.id));
                //通过id查询短信类型并赋值
                entity.setMould_type(dao.getSmsType(dt.id));//dt.type
                entity.setMould_name(dt.title);
                mnum = getTplInfoByNum(dt.id);
                if(null == mnum || mnum == 0){
                    entity.setApply_causes(dt.title);
                    entity.setMould_author(1);
                    dao.insert(entity);
                }else{
                    dao.updateReplyResult(entity);
                }

            }
        }
    }

    public Integer getTplInfoByNum(Integer id){
        Integer snum = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("mould_num",id);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            snum = Integer.valueOf(mp.get("mould_num").toString());
        }
        return snum;
    }

    public int getSmsType(int moudle_num){
        return dao.getSmsType(moudle_num);
    }
}
