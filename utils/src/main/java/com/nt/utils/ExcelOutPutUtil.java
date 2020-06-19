package com.nt.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nt.utils.jxlsUtil.JxlsBuilder;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExcelOutPutUtil {

    public static void OutPut(String fileName, String templetName, Map<String, Object> data, HttpServletResponse response) throws Exception {

        ClassPathResource resource = new ClassPathResource("jxls_templates");

        String imgRoot = AuthConstants.FILE_DIRECTORY + "image";

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

        jxlsBuilder.build();
    }

    public static void OutPutPdf(String fileName, String templetName, Map<String, Object> data, HttpServletResponse response) throws Exception {

        ClassPathResource resource = new ClassPathResource("jxls_templates");

        String imgRoot = AuthConstants.FILE_DIRECTORY + "image";

//        ClassPathResource Pdf_resource = new ClassPathResource("pdf_files");

//        String pdfRoot = ExcelOutPutUtil.class.getClassLoader().getResource("pdf_files").getPath();
        String pdfRoot = AuthConstants.FILE_DIRECTORY + "pdf";
                response.setContentType(java.net.URLEncoder.encode(fileName, "UTF-8"));
        ServletOutputStream out=response.getOutputStream();

        JxlsBuilder jxlsBuilder = JxlsBuilder
                /* -- 加载模板方式 -- */
                //使用路径加载模板，可以是相对路径，也可以绝对路径
                .getBuilder(templetName)
                /* -- 输出文件方式 -- */
                //指定输出的文件流
//                .out(out)
                .out(pdfRoot + "/" + templetName)
                //设置图片路径的根目录
                .imageRoot(imgRoot)
                //设置如果图片缺失不终止生成
                .ignoreImageMiss(true);

        for(Map.Entry<String, Object> entry : data.entrySet()){
            jxlsBuilder.putVar(entry.getKey(),entry.getValue());
        }

        jxlsBuilder.build();

        new jacob2pdf().excel2Pdf(pdfRoot + "/" + templetName,
                pdfRoot + "/" + templetName.split("\\.")[0]+".pdf");


        FileInputStream fileInput = new FileInputStream(pdfRoot + "/" + templetName.split("\\.")[0]+".pdf");
        int i = fileInput.available();
        byte[] content = new byte[i];
        fileInput.read(content);

        out.write(content);
        out.flush();
        fileInput.close();
        out.close();

        FileUtil.del(pdfRoot + "/" + templetName);
        FileUtil.del(pdfRoot + "/" + templetName.split("\\.")[0]+".pdf");
    }

    public static boolean deleteDir(String path) {
        File file = new File(path);
        if (!file.exists()) {//判断是否待删除目录是否存在
            System.err.println("The dir are not exists!");
            return false;
        }

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for (String name : content) {
            File temp = new File(path, name);
            if (temp.isDirectory()) {//判断是否是目录
                deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            } else {
                if (!temp.delete()) {//直接删除文件
                    System.err.println("Failed to delete " + name);
                }
            }
        }
        return true;
    }
}
