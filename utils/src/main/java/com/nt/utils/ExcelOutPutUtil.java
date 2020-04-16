package com.nt.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.nt.utils.jxlsUtil.JxlsBuilder;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelOutPutUtil {

    public static void OutPut(String fileName, String templetName, Map<String, Object> data, HttpServletResponse response) throws Exception {

        ClassPathResource resource = new ClassPathResource("jxls_templates");

        String imgRoot = resource.getPath();

//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
        response.setContentType(java.net.URLEncoder.encode(fileName, "UTF-8"));
        ServletOutputStream out=response.getOutputStream();

        JxlsBuilder jxlsBuilder = JxlsBuilder
                /* -- 加载模板方式 -- */
                //使用路径加载模板，可以是相对路径，也可以绝对路径
                .getBuilder(templetName)
                /* -- 输出文件方式 -- */
                //指定输出的文件流
               .out(out)
                //设置图片路径的根目录
                .imageRoot(imgRoot)
                //设置如果图片缺失不终止生成
                .ignoreImageMiss(true);

        for(Map.Entry<String, Object> entry : data.entrySet()){
            jxlsBuilder.putVar(entry.getKey(),entry.getValue());
        }


    }

    public static void OutPut1(String fileName, String templetName, Map<String, Object> data,HttpServletResponse response) throws Exception {

        ClassPathResource resource = new ClassPathResource("jxls_templates");

        String imgRoot = resource.getPath();

//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
      //  response.setContentType(java.net.URLEncoder.encode(fileName, "UTF-8"));
      //  ServletOutputStream out = response.getOutputStream();
        FileOutputStream out = new FileOutputStream("");
        JxlsBuilder jxlsBuilder = JxlsBuilder
                /* -- 加载模板方式 -- */
                //使用路径加载模板，可以是相对路径，也可以绝对路径
                .getBuilder(templetName)
                /* -- 输出文件方式 -- */
                //指定输出的文件流
                .out(out)
                //设置图片路径的根目录
                .imageRoot(imgRoot)
                //设置如果图片缺失不终止生成
                .ignoreImageMiss(true);

        for(Map.Entry<String, Object> entry : data.entrySet()){
            jxlsBuilder.putVar(entry.getKey(),entry.getValue());
        }

        jxlsBuilder.build();



        //File pdfFile = new File("C:\\Users\\Administrator\\Desktop\\jiaban.pdf");// 输出路径
        Workbook wb = new Workbook("C:\\Users\\Administrator\\Desktop\\jiaban.xlsx");// 原始excel路径
        //FileOutputStream fileOS = new FileOutputStream(pdfFile);
        wb.save(response.getOutputStream(), SaveFormat.PDF);
        //fileOS.close();
    }

    private static void copyStream(InputStream ips,OutputStream ops) throws Exception {
        byte[] buf = new byte[1024];
        int len = ips.read(buf);
        while(len != -1) {
            ops.write(buf,0,len);
            len = ips.read(buf);
        }
    }


}
