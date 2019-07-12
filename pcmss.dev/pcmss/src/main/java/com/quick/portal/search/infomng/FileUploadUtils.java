package com.quick.portal.search.infomng;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.quick.core.util.common.QCommon;
import com.quick.portal.security.authority.metric.PropertiesUtil;
import com.quick.portal.sms.smsServices.SmsConstants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


/**
 * 上传文件工具类 
 * 创建时间：
 * 
 * @version
 */
public class FileUploadUtils {

    /**
     * @param file
     *            //文件对象
     * @param filePath
     *            //上传路径
     * @param fileName
     *            //文件名
     * @return 文件名
     */
    public static String fileUp(MultipartFile file, String filePath, String fileName) {
        String extName = ""; // 扩展名格式：
        try {
            if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
                extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }
            copyFile(file.getInputStream(), filePath, fileName + extName).replaceAll("-", "");
        } catch (IOException e) {
            System.out.println(e);
        }
        return fileName + extName;
    }

    /**
     * 写文件到当前目录的upload目录中
     * 
     * @param in
     * param fileName
     * @throws IOException
     */
    private static String copyFile(InputStream in, String dir, String realName) throws IOException {
        File file = new File(dir, realName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        System.out.println(file.getAbsolutePath());
        FileUtils.copyInputStreamToFile(in, file);
        return realName;
    }

    /**
     * @param request
     *            //requst请求
     * @param filePath
     *            //上传路径
     * @param fileName
     *            //文件名
     * @return 文件名
     * @throws IOException
     * @throws IllegalStateException
     */
    public static String upload(HttpServletRequest request, String filePath, String fileName)
            throws IllegalStateException, IOException {
        String extName = ""; // 扩展名格式：
        String path = "";
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    // 取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (myFileName.trim() != "") {
                        // 重命名上传后的文件名
                        if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
                            extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                        }
                        // 定义上传路径
                        path = filePath + "/" + fileName + extName;
                        // System.out.println("---------------------");
                        // System.out.println(path);
                        // System.out.println("---------------------");
                        File localFile = new File(path);
                        file.transferTo(localFile);
                    }
                }
            }

        }
        return extName;
    }
    
    

    /**
     * @param request
     *            //requst请求
     * @param filePath
     *            //上传路径
     * @param fileName
     *            //文件名
     * @return 文件名
     * @throws IOException
     * @throws IllegalStateException
     */
    public static String[] uploadMultiple(HttpServletRequest request, String filePath, String[] fileName)
            throws IllegalStateException, IOException {
        String[] extName = new String[fileName.length]; // 扩展名格式：
        String path = "";
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            int i = 0;
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    // 取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (myFileName.trim() != "") {
                        // 重命名上传后的文件名
                        if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
                            extName[i] = file.getOriginalFilename()
                                    .substring(file.getOriginalFilename().lastIndexOf("."));
                        }
                        // 定义上传路径
                        fileName[i] = fileName[i].replace(extName[i], "");
                        path = filePath + "/" + fileName[i] + extName[i];
//                        path = fileName[i] + extName[i];
                        File localFile = new File(path);
                        file.transferTo(localFile);
                        System.out.println(localFile.getAbsolutePath());
                    }
                }
                i++;
            }

        }
        return extName;
    }

    /**
     * 从request那里得到所有MultipartFile
     */
    public static List<MultipartFile> getMultipartFileFromRequest(HttpServletRequest request)
            throws IllegalStateException, IOException {
        
        List<MultipartFile> fileList = new ArrayList<MultipartFile>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                System.out.println(file.getName()+"11111111111");
                if (file != null && file.getSize() > 0) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
    /**
     * @Description:删除文件
     */
    public static void deleteFile(String bucketName, String key) {
        return;
    }
    public static void deleteFile(String bucketName) {
        return;
    }
    
    /**
     * 从request那里得到所有MultipartFile
     */
    public static MultipartFile getMultipartFileByName(HttpServletRequest request,String name)
            throws Exception {
        MultipartFile file=null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                file = multiRequest.getFile(iter.next());
                if (file != null&&file.getName().equals(name)) {
                    return file;
                }
            }
        }
        return file;
    }
    
    /**
     * 根据文件得到后缀名
     * @param file
     * @return
     */
    public static String getExtNameByString(String file){
        String extName="";
        if (file.lastIndexOf(".") >= 0){
            extName = file.substring(file.lastIndexOf("."));
        }
        return extName;
    }
    
    public static String getExtName(MultipartFile file){
        String extName="";
        if (file.getOriginalFilename().lastIndexOf(".") >= 0){
            extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        }
        return extName;
    }
    public static String getImgUploadPath(HttpServletRequest request) {
        //将request变成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        //创建文件夹
        //       String baseDir = PropertiesUtil.getPropery("file.dir");
        String baseDir  = SmsConstants.TARGE_UPLOAD_PATH;
 //       String baseDir  = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/contentRule");
        File dirPath = new File(baseDir + SmsConstants.SRC_UPLOAD_PATH);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        //获取multiRequest 中所有的文件名
        Iterator iter = multiRequest.getFileNames();
        try {
            while (iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    //上传
                    // 获取文件名后缀
                    String oldname = file.getOriginalFilename();
                    String suffix = oldname.indexOf(".") != -1 ? oldname.substring(oldname.lastIndexOf(".")) : "";
                    String fname = QCommon.getUUID() + suffix;
///                    String fname = oldname;
                    File uploadFile = new File(dirPath + File.separator + fname);
                    FileCopyUtils.copy(file.getBytes(), uploadFile);
                    String url = SmsConstants.TARGE_UPLOAD_PATH + SmsConstants.SRC_UPLOAD_PATH + fname;
                    return url;
                }
            }
        } catch (Exception e) {
            System.out.print("[getImgUploadPath]无法保存上传文件:" + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
//    private final static String TARGE_UPLOAD_PATH =  "C:/puchenfile";//PropertiesUtil.getPropery("potal.TARGE_UPLOAD_PATH");

//    private final static String SRC_UPLOAD_PATH = "/phoneNumber/";
}