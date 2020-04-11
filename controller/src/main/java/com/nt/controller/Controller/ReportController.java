package com.nt.controller.Controller;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private DataSource dataSource;


    /**
     * 转换为pdf展示
     *
     * @param response
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws JRException
     * @throws IOException
     */
    @RequestMapping(value="/demo",method = {RequestMethod.GET})
    public void getReportByParam(
            HttpServletResponse response) throws SQLException, ClassNotFoundException, JRException, IOException {

        Map<String, Object> parameters = new HashMap<>();
        //获取文件流
        ClassPathResource resource = new ClassPathResource("jaspers" + File.separator + "demo.jasper");
        InputStream jasperStream = resource.getInputStream();

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline;");
        final OutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }
}
