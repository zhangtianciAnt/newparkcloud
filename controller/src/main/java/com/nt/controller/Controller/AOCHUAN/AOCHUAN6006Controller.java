package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;

import com.nt.service_AOCHUAN.AOCHUAN6000.ReimbursementService;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/reimbursement")
public class AOCHUAN6006Controller {

    @Autowired
    private ReimbursementService reimbursementService;

    /**
     * 获取projects表数据
     */
    @RequestMapping(value = "/getReimbursementList", method = {RequestMethod.POST})
    public ApiResult getReimbursementList(HttpServletRequest request) throws Exception {
        return ApiResult.success(reimbursementService.getReimbursementList());
    }
}
