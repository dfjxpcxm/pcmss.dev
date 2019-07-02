package com.quick.portal.sms.smsServices;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Base64;


public class FileCodeForBase64{

    /**
            * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
            * @CreateTime:
            * @return
            */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }


    /**
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     * @param imgStr base64编码字符串
     * @param path 图片路径-具体到文件
     * @return
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        // 解密
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 示例
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    @SuppressWarnings("resource")
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
        String strImg = getImageStr("c:\\1\\1.jpg");
        System.out.println(strImg);
        File file = new File("c://1.txt");
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        try {
            osw.write(strImg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //generateImage(strImg, "Z:\\水印\\444.bmp");

    }
}

