package com.nt.controller.Controller.AOCHUAN;
import com.nt.service_Org.LogService;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.service_AOCHUAN.AOCHUAN3000.AOCHUAN3009Service;

@RestController
@RequestMapping("/AOCHUAN3009")
public class AOCHUAN3009Controller {

    @Autowired
    private  AOCHUAN3009Service AoChuan3009Serivce;

    /**
     * 获取projectschedule表数据
     */
    @RequestMapping(value = "/getProjectList", method = {RequestMethod.POST})
    public ApiResult getProjectList() throws Exception {
        return ApiResult.success(AoChuan3009Serivce.getProjectList());
    }
}
