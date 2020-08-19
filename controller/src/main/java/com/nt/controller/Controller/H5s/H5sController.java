package com.nt.controller.Controller.H5s;

import com.nt.utils.ApiResult;
import com.nt.utils.CowBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * h5s相关 controller
 */
@RestController
@RequestMapping("/h5s")
public class H5sController {

    @Autowired
    private CowBUtils cowBUtils;

    @RequestMapping(value = "/getH5sSession", method = {RequestMethod.GET})
    public ApiResult getH5sSession() throws Exception {
        String session = cowBUtils.getH5sSession();
        return ApiResult.success(session);
    }
}
