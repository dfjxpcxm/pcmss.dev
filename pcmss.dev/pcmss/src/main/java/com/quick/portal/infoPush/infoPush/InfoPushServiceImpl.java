/**
 * <h3>标题 : potal统一门户-page </h3>
 * <h3>描述 : page服务实现类</h3>
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
package com.quick.portal.infoPush.infoPush;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.quick.portal.security.synchrodata.bjcadata.SynchronizedDataConstants;
import com.quick.portal.security.synchrodata.internal.Dom4jUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quick.core.base.ISysBaseDao;
import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.DateTime;

/**
 * page服务实现类
 */
 @Transactional
 @Service("infoPushService")
public class InfoPushServiceImpl extends SysBaseService<InfoPushDO> implements IInfoPushService {
    
    /**
     * 构造函数
     */
    public InfoPushServiceImpl() {
        BaseTable = "index_info";
        BaseComment = "index_info";
        PrimaryKey = "index_id";
        NameKey = "index_id";
    }
    
    @Autowired
    private IInfoPushDao dao;

   
    
    @Override
    public ISysBaseDao getDao(){
        return dao;
    }
    


    /*
     * 通过用户帐号获取用户行为数据接口（指标接口），返回报文数据
     */
    @Override
    public String getUserBehaviorDataByUserID(String userID) {
        List<Map<String, Object>> retList = dao.getUserBehaviorDataByUserID(userID);
        String xml = getXmlInstall(retList) ;
        return xml;
    }


    public static String getXmlInstall (List<Map<String, Object>> retList ){
        String xml = "";
        if(retList.isEmpty() || retList.size()==0){
            xml = Dom4jUtil.creatErrXmlFile(SynchronizedDataConstants.FAIL_STATUS,SynchronizedDataConstants.DATA_ISNULL_FAIL_MSG);
        }else{
            try {
                xml = Dom4jUtil.writeFormatXML(retList);
            } catch (IOException e) {
                xml = Dom4jUtil.creatErrXmlFile(SynchronizedDataConstants.FAIL_STATUS,e.getLocalizedMessage());
                // TODO Auto-generated catch block
            } catch (DocumentException e) {
                // TODO Auto-generated catch block
                xml = Dom4jUtil.creatErrXmlFile(SynchronizedDataConstants.FAIL_STATUS,e.getLocalizedMessage());
            }
        }
        return xml;
    }

    /*
     * 通过用户帐号获取资源目录数据接口，返回报文数据
     */
    @Override
    public String getCataDataByUserID(String userID){
        List<Map<String, Object>> retList = dao.getCataDataByUserID(userID);
        String xml = getXmlInstall(retList) ;
        return xml;
    }


}