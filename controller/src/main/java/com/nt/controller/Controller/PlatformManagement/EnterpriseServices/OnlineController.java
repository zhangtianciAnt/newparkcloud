package com.nt.controller.Controller.PlatformManagement.EnterpriseServices;

import com.nt.service_Online.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    在线申请Controller
 */
@RestController
@RequestMapping("/onlineApplication")
public class OnlineController {
    @Autowired
    private OnlineService onlineService;
}
