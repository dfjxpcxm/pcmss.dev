/**
 * <h3>标题 : Quick通用系统框架 </h3>
 * <h3>描述 : 应用中的相关配置信息都放在此</h3>
 * <h3>日期 : 2014-03-23</h3>
 * <h3>版权 : Copyright (C) 海口鑫网计算机网络有限公司</h3>
 * 
 * <p>
 * @author admin admin@xinwing.com.cn
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
package com.quick.portal.web.mainframe;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quick.core.base.SysBaseService;

/**
 * 查询菜单权限
 * @author Administrator
 */
@Service("mainFrameService")
public class MainFrameServiceImpl extends SysBaseService<MainFrameBean> implements MainFrameService {

    @Autowired
    private MainFrameDao dao;

	@Override
	public List<MainFrameBean> searchMainFrame(String userId) {
		// TODO Auto-generated method stub
		Integer uid = Integer.valueOf(userId);
		List<MainFrameBean> mainFrameList= dao.searchMainFrame(uid);
		return mainFrameList;
	}

	@Override
	public Map<String, Object> searchSysIndex(int role_type_id) {

		return dao.searchSysIndex(role_type_id);
	}

	@Override
	public AcessData2MainBean getAcessData2Main(Map<String, Object> mp) {
		AcessData2MainBean ad = new AcessData2MainBean ();
		List<Map<String, Object>>  retList = dao.getAcessData2Main(mp);
		if(!retList.isEmpty() && retList.size()>0){
			Map<String, Object> mm = retList.get(0);
			ad.setApp_count(Integer.parseInt(mm.get("app_count").toString()));
			ad.setUser_count(Integer.parseInt(mm.get("user_count").toString()));
			ad.setIndex_count(Integer.parseInt(mm.get("index_count").toString()));
			ad.setJob_count(Integer.parseInt(mm.get("job_count").toString()));
		}
		return ad;
	}

	@Override
	public JobDataBean getJobData(Map<String, Object> mp) {
		JobDataBean job = new JobDataBean();
		List<Map<String, Object>>  retList = dao.getJobData(mp);
		if(!retList.isEmpty() && retList.size()>0){
			Map<String, Object> mm = retList.get(0);
			job.setJob_fail_count(Integer.parseInt(mm.get("JOB_FAIL_COUNT").toString()));
			job.setJob_suc_count(Integer.parseInt(mm.get("JOB_SUC_COUNT").toString()));

		}
		return job;
	}

	@Override
	public List<Integer> getLoginCntData(Map<String, Object> mp) {
		List<Integer> logCnt = new ArrayList<>();
		List<Map<String, Object>>  retList = dao.getLoginCntData(mp);
		if(!retList.isEmpty() && retList.size()>0){
			for(Map<String, Object> mm:retList){
				logCnt.add(Integer.parseInt(mm.get("LOGIN_COUNT").toString()));
			}

		}
		return logCnt;
	}

}
