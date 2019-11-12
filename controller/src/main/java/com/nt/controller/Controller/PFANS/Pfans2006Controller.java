package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.InterviewRecord;
import com.nt.service_pfans.PFANS2000.InterviewRecordService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/interviewrecord")
public class Pfans2006Controller {
	
    @Autowired
    private TokenService tokenService;

}