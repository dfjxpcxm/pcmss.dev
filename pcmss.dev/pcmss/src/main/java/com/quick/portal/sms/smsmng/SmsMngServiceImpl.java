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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.ReflectUtil;
import com.quick.portal.search.infomng.ExcelResolve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * sys_glabal_parm服务实现类
 */
@Transactional
@Service("smsMngService")
public class SmsMngServiceImpl extends SysBaseService<SmsMngDO> implements ISmsMngService {

    /**
     * 构造函数
     */
    @Autowired
    public SmsMngServiceImpl(ISmsMngDao<SmsMngDO> dao) {
        BaseTable = "sys_sms_info";
        BaseComment = "sys_sms_info";
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
    private Class<SmsMngDO> entityClass;
    @Transactional
    @Override
    public DataStore save(SmsMngDO entity) {

        ExcelResolve excelResolve = new ExcelResolve();
        String filePath =entity.getFilePath();//"C:/Users/angxia1981/Downloads/puchenfile/phoneNumber/ffa2c7f57e4e464199d05b567ef1b398.xlsx";
        File f1 = new File(filePath);
        try {
            List<SmsMngDO> PhoneNUmList = new ArrayList<SmsMngDO>();
            System.out.println(excelResolve.readExcel(f1));
            JSONArray array = excelResolve.readExcel(f1);
            for (int i = 0; i < array.size(); i++) {
                JSONObject job = array.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                //System.out.println(job.get("number")) ;  // 得到 每个对象中的属性值
                SmsMngDO smsMngDO = new SmsMngDO();
                smsMngDO.setPhone_num((String) job.get("手机号"));
                PhoneNUmList.add(smsMngDO);
            }
            dao.insertPhoneNUmList(PhoneNUmList);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String key = getPrimaryKey();
        Integer keyVal = entity.getSms_id();
        /*
        用于正则表达式匹配，提取，替换，拼接
        String str = "";
        String sss="\\$"+"\\{+[a-zA-Z]+}";
        String content=entity.getMould_content();
        String mould_fie[] = entity.getMould_fields().split(",");
        Pattern pattern = Pattern.compile(sss);
        Matcher matcher = pattern.matcher(entity.getMould_content());
        int i = 0;
        while(matcher.find()){
            //str += matcher.group();
                content = content.replace(matcher.group(),mould_fie[i]);
                i++;
        }
        entity.setSms_content(content);*/
        int c = 0;
        if (keyVal == null) {
            //如果有新增时间，维护对应字段
//            ReflectUtil.trySetValue(entity, "cre_time", DateTime.Now().getTime(), entityClass);
            //如果有修改时间，维护对应字段
            //           ReflectUtil.trySetValue(entity, "upd_time", DateTime.Now().getTime(), entityClass);
            c = dao.insert(entity);
        } else {
            //如果有修改时间，维护对应字段
            //           ReflectUtil.trySetValue(entity, "upd_time", DateTime.Now().getTime(), entityClass);
            c = dao.update(entity);
        }

        if (c == 0) {
            int error = Integer.valueOf(
                    ReflectUtil.getValue(entity, "error_no", entityClass).toString());
            if (error == 0)
                return ActionMsg.setError("操作失败");
        }

        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }
}