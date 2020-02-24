package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21211Controller
 * @Author: 王哲
 * @Description: 在线培训申请
 * @Date: 2020/2/17 20:15
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21211")
public class BASF21211Controller {

    @Autowired
    private TokenService tokenService;


}
