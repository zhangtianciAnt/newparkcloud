package com.nt.controller.PHINEController;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.service_PHINE.PHINEFileService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/PHINEFile")
public class PHINEFileController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PHINEFileService phineFileService;

    @RequestMapping(value = "/saveFileInfo", method = {RequestMethod.POST})
    public ApiResult saveFileUrl(HttpServletRequest request, @RequestBody Fileinfo fileInfo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return phineFileService.saveFileInfo(tokenModel, fileInfo);
    }
}
