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
import com.quick.core.util.common.DateTime;
import com.quick.core.util.common.ReflectUtil;
import com.quick.portal.sms.smsmng.SmsMngDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * sys_mould_info服务实现类
 */
@Transactional
@Service("mouldMngService")
public class MouldMngServiceImpl extends SysBaseService<MouldMngDO> implements IMouldMngService {

    /**
     * 构造函数
     */
    @Autowired
    public MouldMngServiceImpl(IMouldMngDao<MouldMngDO> dao) {
        BaseTable = "sys_mould_info";
        BaseComment = "sys_mould_info";
        PrimaryKey = "mould_id";
        NameKey = "mould_id";

        setDao(dao);
        this.dao = dao;
    }

    private IMouldMngDao<MouldMngDO> dao;


    @Override
    public List<Map<String, Object>> getMouldTypeData() {
        return dao.getMouldTypeData();
    }

    @Override
    public List<Map<String, Object>> getComMouldData()
        {
            return dao.getComMouldData();
    }

    private Class<SmsMngDO> entityClass;
    @Transactional
    @Override
    public DataStore save(MouldMngDO entity) {
        //如果编号为空,新增实体对象,否则更新实体对象
        String key = getPrimaryKey();
        Integer keyVal = entity.getMould_id();
        String str = "";
        String reg="\\$\\{+[a-zA-Z]+}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(entity.getMould_content());
        while(matcher.find()){
            str += matcher.group()+",";
        }
        String str_sub=str.substring(0,str.length()-1);
        entity.setMould_fields(str_sub);
        int c = 0;
        if (keyVal == null) {
            //如果有新增时间，维护对应字段
            ReflectUtil.trySetValue(entity, "cre_time", DateTime.Now().getTime(), entityClass);
            //如果有修改时间，维护对应字段
            ReflectUtil.trySetValue(entity, "upd_time", DateTime.Now().getTime(), entityClass);
            c = dao.insert(entity);
        } else {
            //如果有修改时间，维护对应字段
            ReflectUtil.trySetValue(entity, "upd_time", DateTime.Now().getTime(), entityClass);
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