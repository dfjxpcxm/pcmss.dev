package com.quick;

import com.quick.portal.search.infomng.ExcelResolve;

import java.io.File;

public class M {
    public static void main(String[] args) {
        ExcelResolve excelResolve =new ExcelResolve();
        File f1 = new File("C:/Users/angxia1981/Downloads/puchenfile/phoneNumber/ffa2c7f57e4e464199d05b567ef1b398.xlsx");

        try {
            System.out.println(excelResolve.readExcel(f1));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
