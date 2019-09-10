/**
 * <h3>标题 : potal统一门户-section </h3>
 * <h3>描述 : section服务实现类</h3>
 * <h3>日期 : 2018-05-03</h3>
 * <h3>版权 : Copyright (C) 北京东方金信科技有限公司</h3>
 * 
 * <p>
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
package com.quick.portal.newsMessage;

import com.quick.core.base.ISysBaseDao;
import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.base.model.PageBounds;
import com.quick.core.util.common.DateTime;
import com.quick.core.util.common.QCommon;
import com.quick.portal.secMetricConfig.ISecMetricConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.HEAD;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * section服务实现类
 */
 @Transactional
 @Service("newsMessageService")
public class NewsMessageServiceImpl extends SysBaseService<NewsMessageDO> implements INewsMessageService {
    
    /**
     * 构造函数
     */
    public NewsMessageServiceImpl() {
        BaseTable = "res_info";
        BaseComment = "res_info";
        PrimaryKey = "resource_cd";
        NameKey = "resource_cd";
    }

    
    @Autowired
    private INewsMessageDao dao;
    

    
    @Override
    public ISysBaseDao getDao(){
        return dao;
    }
    

    @Override
    public boolean searchResourceInfo(NewsMessageDO msgDO){
            boolean bool = false;
            Integer cnt = dao.searchResourceInfo(msgDO);
            if (cnt > 0) {
                bool = true;
            }
            return bool;
    }




    @Override
    public void updateResourceInfo(NewsMessageDO msgDO) {
         dao.update(msgDO);
    }

    @Override
    public void insertResourceInfo(NewsMessageDO msgDO){
            dao.insert(msgDO);
        }

     @Override
    public List<Map<String, Object>> messageStatu(PageBounds page) {
        return dao.messageStatu(page);
    }

    @Override
    public int update(NewsMessageDO msgDO) {
        return dao.update(msgDO);
    }
}