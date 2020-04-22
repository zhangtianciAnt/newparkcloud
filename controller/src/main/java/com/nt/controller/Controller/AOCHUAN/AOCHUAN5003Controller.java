package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.FinReport;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinReportService;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/finreport")
public class AOCHUAN5003Controller {

    @Autowired
    private FinReportService finReportService;

    /**
     * 获取走货数据
     */
    @RequestMapping(value = "/getAll", method = {RequestMethod.POST})
    public ApiResult getAll(HttpServletRequest request) throws Exception {

        return ApiResult.success(finReportService.getAll());
    }

}
