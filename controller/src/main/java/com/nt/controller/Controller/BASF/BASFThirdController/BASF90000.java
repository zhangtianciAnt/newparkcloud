package com.nt.controller.Controller.BASF.BASFThirdController;

import com.nt.utils.ApiResult;
import org.springframework.web.bind.annotation.*;

/*
    获取第三方接口数据Controller
 */
@RestController
@RequestMapping("/BASF90000")
public class BASF90000 {
    /*
        获取分厂报警信号测试接口
     */
    @RequestMapping(value = "/getInfo", method = {RequestMethod.GET})
    public ApiResult get(@RequestParam String info) throws Exception {
        System.out.println(info);
        return ApiResult.success();
    }
}
