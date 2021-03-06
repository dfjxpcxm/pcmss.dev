package com.quick.portal.search.infomng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.quick.core.base.model.PageBounds;
import com.quick.core.util.common.DateTime;
import com.quick.portal.security.authority.metric.MetricPrivilegeConstants;
import com.quick.portal.security.authority.metric.PropertiesUtil;

/**
 * Solr7 操作 solr版本是7.3.0 
 * solr-solrj 使用的是5.0.0
 */
public class SolrUtils {
	
	private HttpSolrClient server = null;

	

	/**
	 * 添加文档 用solrJ创建索引
	 * @throws Exception 
	 */
	public static void addSolrInfo(String id, String content, String type,String title,String attachID) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", id);
		doc.addField("content",content);
		doc.addField("_text_", content);
		doc.addField("create_time", DateTime.Now().getTime());
		doc.addField("portal_doc_class", type);
		doc.addField("portal_doc_title", title);
		doc.addField("portal_attachment_id",attachID);
		
		HttpSolrClient server = null;
		String url = getSolrServiceUrl();
		try {
		    server = InitSolrServer.initServer(url);
			UpdateResponse response = server.add(doc);
			// 提交
			server.commit();
			System.out.println("########## Query Time :" + response.getQTime());
			System.out.println("########## Elapsed Time :"
					+ response.getElapsedTime());
			System.out.println("########## Status :" + response.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	/**
	 * 单个id 的删除索引
	 */
	public static void deleteSolrInfo(String id) throws Exception {
		String url = getSolrServiceUrl();
		// [1]获取连接
		HttpSolrClient server = InitSolrServer.initServer(url);
		// [2]通过id删除
		server.deleteById(id);
		// [3]提交
		server.commit();
	}
	


	/**
	 * 多个id 的list集合 删除索引
	 */
	public static void deleteBatchSolrList(ArrayList<String> ids) throws Exception {
		String url = getSolrServiceUrl();
		// [1]获取连接
		HttpSolrClient server = InitSolrServer.initServer(url);
		// [2]通过id删除
		for (String id : ids) {
			server.deleteById(id);
		}
		// [3]提交
		server.commit();
	}
	
	/*
	 * 查询SOLR数据
	 * termCd--0:手机APP
	 */
	public static List searchInfoDataByCondition(Map<String, Object> m,
			PageBounds page, List<Map<String, Object>> retList, String type,String termCd) {
		List restList = new ArrayList();
		if (SolrInfoConstants.UNREAD_OBJ_TYPE.equals(type)) {
			int fromIndex = page.getStartRow() == 1 ? 0 : page.getStartRow() - 1;
			int toIndex = page.getEndRow();
			SolrDocumentList docInfoList = (SolrDocumentList) getUnReadData(m,retList);
			List dataList = getUnReadInfoData(docInfoList,termCd);
			restList = getDataList(dataList, fromIndex,toIndex);
		}else if (SolrInfoConstants.READ_OBJ_TYPE.equals(type)) {
			 int fromIndex = page.getStartRow() == 1 ? 0 : page.getStartRow() - 1;
			 int toIndex = page.getEndRow();
			 restList = getReadInfoData(m,retList,fromIndex,toIndex,termCd);
		}else{
			SolrQuery query = getAllSolrQuery(m, page,type);
			SolrDocumentList docList = getSolrInfoDataByTitle(query);
			restList = getAllInfoData(docList,retList,termCd);
		}
	
		return restList;
	}
	


	/*
	 * 未查阅数据
	 *  去掉已阅数据
	 */
	public static List getUnReadData(Map<String, Object> m,List<Map<String, Object>> retList) {
		SolrQuery query= getSolrQueryByCond(m);
		SolrDocumentList docList = getSolrInfoDataByTitle(query);
		String id = null;
		String cid = null;
		System.out.println("####### 未查阅数据总共 ： " + docList.getNumFound() + "条记录");
		for (Map<String, Object> data : retList) {
			cid = data.get("MSG_CONTENT").toString();
			for (SolrDocument doc : docList) {
				System.out.println("#######未查阅数据 id : " + doc.get("id")
						+ "  title : " + doc.get("portal_doc_title")
						+ "  portal_doc_class : " + doc.get("portal_doc_class"));
				id = doc.get("id") == null ? "" : doc.get("id").toString();
				if (null != cid && cid.equals(id)) {
					docList.remove(doc);
					break;
				}
			}
		}
		return docList;
	}
	
	public static List getUnReadInfoData(SolrDocumentList docList,String termCd){
		List restList = new ArrayList();
		List<Map<String, Object>> dataList = new ArrayList<>();
		Map<String, Object> dataMap = null;
		String id = null;
		String aid = null;
		System.out.println("####### 总共 ： " + docList.size() + "条记录");
		for (SolrDocument doc : docList) {
			System.out.println("####### id : " + doc.get("id") + "  title : "
					+ doc.get("portal_doc_title") + "  portal_doc_class : "
					+ doc.get("portal_doc_class") );
			
			id = doc.get("id") == null ? "" : doc.get("id").toString();
			aid = doc.get("portal_attachment_id") == null ? "" : doc.get("portal_attachment_id").toString();
			if(null !=termCd && TERM_TYPE_APP.equals(termCd)){
				id = id.replace(INFO_APP_FILE_DIR, "");
				aid = aid.replace(INFO_APP_FILE_DIR, "");
				String appUrl = PropertiesUtil.getPropery("app.upload.file.dir");
				id = appUrl.concat(id); 
				aid = appUrl.concat(aid);
			}

			dataMap = new HashMap();
			dataMap.put("status", SolrInfoConstants.UNREAD_STATUS);
			dataMap.put("id", id);
			dataMap.put("title",
					doc.get("portal_doc_title") == null ? "" : doc.get("portal_doc_title"));
			dataMap.put("portal_doc_class",
					doc.get("portal_doc_class") == null ? "" : doc.get("portal_doc_class"));
			
			
			dataMap.put("aid",aid);
			
			dataList.add(dataMap);
		}
		Integer count = dataList.size();
		restList.add(count);
		restList.add(dataList);
		return restList;
	}
	
	/*
	 * 全部
	 * 
	 */
	public static SolrQuery getAllSolrQuery(Map<String, Object> m,PageBounds page,String type){
		String queryStr = m.get(SolrInfoConstants.INDEX_KEYWORD).toString();
		if(null == queryStr ||"".equals(queryStr)){
			return null;
		}
		SolrQuery query = new SolrQuery(queryStr);
		query.setStart(page.getStartRow() == 1 ? 0 : page.getStartRow() - 1);
		query.setRows(page.getPageSize());
		if (SolrInfoConstants.INDEX_OBJ_TYPE.equals(type)) {
			query.addFilterQuery("portal_doc_class:"+SolrInfoConstants.INDEX_OBJ_TYPE+"");
		} else if (SolrInfoConstants.MSG_OBJ_TYPE.equals(type)) {
			query.addFilterQuery("portal_doc_class:"+SolrInfoConstants.MSG_OBJ_TYPE+"");
		} else if (SolrInfoConstants.APP_OBJ_TYPE.equals(type)) {
			query.addFilterQuery("portal_doc_class:"+SolrInfoConstants.APP_OBJ_TYPE+"");
		}else if (SolrInfoConstants.DATA_OBJ_TYPE.equals(type)) {
			query.addFilterQuery("portal_doc_class:"+SolrInfoConstants.DATA_OBJ_TYPE+"");
			query.setStart(SolrInfoConstants.PAGE_START);
			query.setRows(SolrInfoConstants.PAGE_ROWS);
		}  
		// 排序 如果按照blogId 排序，那么将blogId desc(or asc) 改成 id desc(or asc)
//		query.addSort("create_time", ORDER.asc);
		return query;
	}
	
	/*
	 * 未阅、已阅
	 * 
	 */
	public static SolrQuery getSolrQueryByCond(Map<String, Object> m){
		String queryStr = m.get(SolrInfoConstants.INDEX_KEYWORD).toString();
		SolrQuery query = new SolrQuery(queryStr);
		query.setStart(SolrInfoConstants.PAGE_START);
		query.setRows(SolrInfoConstants.PAGE_ROWS);
		return query;
	}
	
	/*
	 * 连接SOLR服务
	 * 
	 */
	public static SolrDocumentList getSolrInfoDataByID(SolrQuery query,String id){
		QueryResponse response = null;
		String url = getSolrServiceUrl();
		try {
			HttpSolrClient server = InitSolrServer.initServer(url);
			response = server.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SolrDocumentList docList = response.getResults();
		
		
		return docList;
	}
	
	/*
	 * 通过关键字规则判断，有关键字返回TRUE; 没有关键字返回FALSE;
	 * 
	 */
	public static boolean getSolrDataByRule(SolrDocumentList docList,String key){
		boolean  bool = false;
		String id = null;
		System.out.println("####### 总共 ： " + docList.size() + "条记录");
		for (SolrDocument doc : docList) {
			System.out.println("####### id : " + doc.get("id") + "  title : "
					+ doc.get("portal_doc_title") + "  portal_doc_class : "
					+ doc.get("portal_doc_class") );
			id = doc.get("id") == null ? "" : doc.get("id").toString();
			if(null !=key && key.equals(id)){
			    bool = true;
				return bool;
			}
		}
		return bool;
	}
	
	
	
	
	public static SolrDocumentList getSolrInfoDataByTitle(SolrQuery query){
		QueryResponse response = null;
		String url = getSolrServiceUrl();
		try {
			HttpSolrClient server = InitSolrServer.initServer(url);
			response = server.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SolrDocumentList docList = response.getResults();
		return docList;
	}
	
	

	/*
	 * 已查阅数据
	 * 与查阅数据比对
	 */
	public static List getReadInfoData(Map<String, Object> m,
			List<Map<String, Object>> retList,int fromIndex, int toIndex,String termCd) {
		List<Map<String, Object>> dataList = new ArrayList<>();
		SolrQuery query= getSolrQueryByCond(m);	
		List<SolrDocument> docList = getSolrInfoDataByTitle(query);
		List restList = getInfoData(docList, retList,termCd);
		List dtList = getDataList(restList, fromIndex,toIndex);
		return dtList;		
	}
	
	
	public static List getInfoData(List<SolrDocument> docList,
			List<Map<String, Object>> retList,String termCd) {
		List restList = new ArrayList();
		Map<String, Object> dataMap = null;
		List<Map<String, Object>> dataList = new ArrayList<>();
		System.out.println("####### 已查阅表关系数据总共 ： " + retList.size() + "条记录");
		String cid = null, id = null, aid = null;
		for (Map<String, Object> data : retList) {
			cid = data.get("MSG_CONTENT").toString();
			dataMap = new HashMap();
			for (SolrDocument doc : docList) {
				System.out.println("####### 查询数据id : " + doc.get("id")
						+ "  title : " + doc.get("portal_doc_title")
						+ "  portal_doc_class : " + doc.get("portal_doc_class"));
				id = doc.get("id") == null ? "" : doc.get("id").toString();
				if (null != cid && cid.equals(id)) {
					// status:1表示已阅
					dataMap.put("status", SolrInfoConstants.READ_STATUS);
					
					aid = doc.get("portal_attachment_id") == null ? "" : doc.get("portal_attachment_id").toString();
					if(null !=termCd && TERM_TYPE_APP.equals(termCd)){
						id = id.replace(INFO_APP_FILE_DIR, "");
						aid = aid.replace(INFO_APP_FILE_DIR, "");
						String appUrl = PropertiesUtil.getPropery("app.upload.file.dir");
						id = appUrl.concat(id); 
						aid = appUrl.concat(aid);
					}
					
					dataMap.put("id", id);
					dataMap.put("title",
							doc.get("portal_doc_title") == null ? "" : doc.get("portal_doc_title"));
					dataMap.put(
							"portal_doc_class",
							doc.get("portal_doc_class") == null ? "" : doc
									.get("portal_doc_class"));
					dataMap.put("aid",aid);
					dataList.add(dataMap);
					break;
				}
			}
		}
		Integer count = dataList.size();
		restList.add(count);
		restList.add(dataList);
		return restList;
	}
	
	
	public static List getDataList(List dataList, int fromIndex,int toIndex) {
		List retList = new ArrayList();
		Integer count = (int)dataList.get(0);
		retList.add(count);
		List dtList  = (List)dataList.get(1);
		if(dtList.size() > toIndex){
			retList.add(dtList.subList(fromIndex, toIndex));
		}else{
			retList.add(dtList.subList(fromIndex, dtList.size()));
		}
		return retList;
	}
	
	
	
	public static List getAllInfoData(SolrDocumentList docList,
			List<Map<String, Object>> retList,String termCd) {
		List restList = new ArrayList();
		Map<String, Object> dataMap = null;
		List<Map<String, Object>> dataList = new ArrayList<>();
		System.out.println("####### 总共 ： " + docList.getNumFound() + "条记录");
		int count = (int) docList.getNumFound();
		String cid = null,aid = null,id = null;
		for (SolrDocument doc : docList) {
			System.out.println("####### id : " + doc.get("id")
					+ "  title : " + doc.get("portal_doc_title")
					+ "  portal_doc_class : " + doc.get("portal_doc_class"));
			id = doc.get("id") == null ? "" : doc.get("id").toString();
			dataMap = new HashMap();
			dataMap.put("status",  SolrInfoConstants.UNREAD_STATUS);
			for (Map<String, Object> data : retList) {
				cid = data.get("MSG_CONTENT").toString();
				if (null != cid && cid.equals(id)) {
					// status:1表示已阅
					dataMap.put("status", SolrInfoConstants.READ_STATUS);
					break;
				}
			}
			aid = doc.get("portal_attachment_id") == null ? "" : doc.get("portal_attachment_id").toString();
			if(null !=termCd && TERM_TYPE_APP.equals(termCd)){
				id = id.replace(INFO_APP_FILE_DIR, "");
				aid = aid.replace(INFO_APP_FILE_DIR, "");
				String appUrl = PropertiesUtil.getPropery("app.upload.file.dir");
				id = appUrl.concat(id); 
				aid = appUrl.concat(aid);
			}
			dataMap.put("id", id);
			dataMap.put("title",
					doc.get("portal_doc_title") == null ? "" : doc.get("portal_doc_title"));
			dataMap.put("portal_doc_class", doc.get("portal_doc_class") == null ? ""
					: doc.get("portal_doc_class"));
			dataMap.put("aid",aid);
			dataList.add(dataMap);
		}
		restList.add(count);
		restList.add(dataList);
		return restList;
	}
	
	/*
	 * 通过关键字查询资料数据
	 */
	public static List<String> getDataByKeyword(String keyword){
		Map<String, Object> m = new HashMap();
		m.put(SolrInfoConstants.INDEX_KEYWORD, keyword);
		PageBounds page = new PageBounds();
		List<String> retList = searchDataByCondition(m,page,SolrInfoConstants.DATA_OBJ_TYPE);
		return retList;
	}
	
	public static List<String>  searchDataByCondition(Map<String, Object> m,
			PageBounds page, String type) {
		SolrQuery query = getAllSolrQuery(m, page,type);
		SolrDocumentList docList = getSolrInfoDataByTitle(query);
		List<String> list = new ArrayList<String>();
		String id = null;
		for (SolrDocument doc : docList) {
			id = doc.get("id") == null ? "" : doc.get("id").toString();
			list.add(id);
		}
		return list;
	}

	public static void main(String[] args) throws Exception {
		SolrUtils st = new SolrUtils();
		// st.testQueryCondition();
/*		Map<String, Object> m = new HashMap();
		m.put("title", "认证");
		PageBounds page = null;
		// List<Map<String, Object>> retList = searchInfoDataByCondition(m,
		// page);
		// SolrUtils.addDoc("qqq","q","4");
		String id ="C:/Users/cxh/Desktop/test/测试文档.docx";
//		st.deleteSolrInfo(id);
		String content = "测试文档";
		String type="1";
//		int i =0;
		String path ="C:/Users/cxh/Desktop/test/";
		for(int i=30;i<200;i++){
			id = path.concat(content).concat(i+"").concat("docx");
			st.addSolrInfo( id,  content, type,content);
		}
		
		ArrayList<String> ids = new ArrayList();
		SolrQuery sq = getSolrQueryNoCond();
		SolrDocumentList docList = getSolrInfoDataByTitle(sq);
		if(docList.size()>0){
			for(SolrDocument sot:docList){
				ids.add(sot.get("id").toString());
				
			}
		}
		System.out.println("---="+ids.size());
		
		deleteBatchSolrList(ids);*/
		String keyword = "测试";
		
		List<String> retList = getDataByKeyword(keyword);
		 for (String key : retList) {  
			   System.out.println("key= "+ key );  
			  } 
		
	/*	 Iterator<Map.Entry<String, Object>> it = (Iterator<Entry<String, Object>>) getDataByKeyword(keyword);;  
		  while (it.hasNext()) {  
		   Map.Entry<String, Object> entry = it.next();  
		   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());  
		  }  */
/*		List<Integer> itList = new ArrayList();
		int j =10;
		for(int i =0;i<10;i++){
			itList.add(i);
		}
		List ret = itList.subList(0, 8);
		List ret1 = itList.subList(1, 10);
		*/
	}
	
	
	public static SolrQuery getSolrQueryNoCond(){
		SolrQuery query = new SolrQuery();  
	    query.setQuery("*:*"); 
	    query.setStart(SolrInfoConstants.PAGE_START);
		query.setRows(SolrInfoConstants.PAGE_ROWS);
		return query;
	}
	
	public static String getSolrServiceUrl(){
		String url = PropertiesUtil.getPropery("solr.service.url");
	  	String port = PropertiesUtil.getPropery("solr.service.port");
	  	String serviceUrl = url.concat(MetricPrivilegeConstants.SERVICE_PORT).concat(port).concat(SolrInfoConstants.SOLR_PORTAL_DOC_SERVICE);
	  	return  serviceUrl;
	}
	
	private final static String  TERM_TYPE_APP = "0";
	
	private final static String  INFO_APP_FILE_DIR = "/home/portal";
}
