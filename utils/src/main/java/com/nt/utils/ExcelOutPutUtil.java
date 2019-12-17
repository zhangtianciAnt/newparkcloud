package com.nt.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import com.nt.utils.jxlsUtil.JxlsBuilder;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelOutPutUtil {

    public static void OutPut(String fileName, String templetName, Map<String, Object> data, HttpServletResponse response) throws Exception {

        ClassPathResource resource = new ClassPathResource("jxls_templates");

        String imgRoot = resource.getPath();
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setContentType(java.net.URLEncoder.encode(fileName, "UTF-8"));
//        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
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

}
