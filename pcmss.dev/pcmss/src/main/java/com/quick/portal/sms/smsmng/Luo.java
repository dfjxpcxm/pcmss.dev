package com.quick.portal.sms.smsmng;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.quick.portal.search.infomng.ExcelResolve;
import com.quick.portal.sms.smsmng.SmsMngDO;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Luo {
    public static void main(String[] args) {
        ExcelResolve excelResolve = new ExcelResolve();
        String filePath = "C:\\Users\\Administrator\\Desktop\\smstpl20190720.csv";
        SmsMngDO smsMngDO = null;
        File f1 = new File(filePath);
        List<SmsMngDO> phoneNUmList = new ArrayList<SmsMngDO>();
        String sss="\\{+[0-9]+}";
        List<String[]> dataList = new ArrayList<String[]>();

        CsvReader reader = null;
        try {
            reader = new CsvReader(filePath,',',Charset.forName("GBK"));
            reader.readHeaders();
            String[] headArray = reader.getHeaders();//获取标题
            int headArrayLength = headArray.length;

            Pattern pattern = Pattern.compile(sss);
            Matcher matcher = null;
            //System.out.println(headArray[0] + headArray[1] + headArray[2]);
            // 逐条读取记录，直至读完
            while (reader.readRecord()) {
                String content="您的登录验证码{1}，请于{2}分钟内填写。如非本人操作，请忽略本短信。";
                matcher = pattern.matcher(content);
                // 读一整行
                System.out.println(reader.getRawRecord());
                // 读这行的第一列
                //System.out.println(reader.get("客户手机号"));
                // 读这行的第二列
               // System.out.println(reader.get(1));

                    int i = 1;
                     String a = "";
                    while(matcher.find()){
                        content = content.replace(matcher.group(),reader.get(i));
                         a += reader.get(i).concat("#");
                        System.out.println(a);
                        i++;
                    }
                /*smsMngDO = new SmsMngDO();
                smsMngDO.setSms_rec_obj(reader.get(0));
                smsMngDO.setSms_content(content);
                smsMngDO.setSms_id(i);
                smsMngDO.setSms_state(1);
                smsMngDO.setSid(i++ +"");
                if (smsMngDO.getSms_rec_obj().equals(reader.get(0))){
                    System.out.println(false);
                }
                phoneNUmList.add(smsMngDO);
*/
            }
            Set<SmsMngDO> set = new HashSet<>(phoneNUmList);
            //System.out.println(phoneNUmList.size());
            //System.out.println(set.size());

            /*JSONArray array = excelResolve.readExcel(f1);
            for (int i = 0; i < array.size(); i++) {
                JSONObject job = array.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                //System.out.println(job.get("number")) ;  // 得到 每个对象中的属性值
                String content="您的登录验证码{1}，请于{2}分钟内填写。如非本人操作，请忽略本短信。";
                Pattern pattern = Pattern.compile(sss);
                Matcher matcher = pattern.matcher(content);
                System.out.println(job.size());
                *//*for (int j = 1; j < 9; j++) {
                    if (job.get("短信内容变量"+j) != null){
                        while(matcher.find()){
                            content = content.replace(matcher.group(),((String) job.get("短信内容变量"+j)));
                        }
                    }
                }*//*
                System.out.println(excelResolve.readExcel(f1));
               // phoneNUmList.add()
            }*/
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (null != reader) {
                reader.close();
            }
        }
    }
}
