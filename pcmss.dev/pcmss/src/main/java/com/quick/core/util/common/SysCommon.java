package com.quick.core.util.common;

import com.quick.portal.security.authority.metric.PropertiesUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SysCommon {

    public static void main(String[] args) {
        String path  = getFilePath();
        System.out.println(path);
    }


    public static String getFilePath(){
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println(os);		//windows 7
        String filePath = "";
        if (os.startsWith("win")) {
            filePath = PropertiesUtil.getPropery("potal.upload.file");
        }else{
            filePath = PropertiesUtil.getPropery("file.dir");
        }
        return filePath;
    }

}