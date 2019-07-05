package com.quick.portal.sms.smsServices;

import com.quick.portal.search.infomng.SolrInfoConstants;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class InitSmsServer {

	
	private static InitSmsServer instance ;
	
	
  
	private static void instance() {
	}

	public static InitSmsServer getInstance() {
		if (instance == null) {
			synchronized (InitSmsServer.class) {
				InitSmsServer temp = instance;
				if (temp == null) {
					synchronized (InitSmsServer.class) {
						temp = new InitSmsServer();
					}
					instance = temp;
				}
			}
		}
		return instance;
	}

    
    public static synchronized HttpSolrClient getServer(HttpSolrClient server, String url){
    	 // 创建 server
	   	 if(server == null){
	   		 server = new HttpSolrClient.Builder(url)
	           .withConnectionTimeout(SolrInfoConstants.CONNECTION_TIMEOUT)
	           .withSocketTimeout(SolrInfoConstants.SOCKET_TIMEOUT)
	           .build();
	   	 }
	     return server;
    }

    


}
