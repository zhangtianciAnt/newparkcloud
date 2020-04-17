package com.nt.utils;

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Excel2pdf {
    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void excel2pdf(String sourceFilePath, String desFilePath) {
//        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
//            return;
//        }
        try {
            File pdfFile = new File(desFilePath);// 输出路径
            Workbook wb = new Workbook(sourceFilePath);// 原始excel路径
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            wb.save(fileOS, SaveFormat.PDF);
            fileOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
