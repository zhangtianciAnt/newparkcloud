package com.nt.controller.PHINEController;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Vo.FileInfoVo;
import com.nt.service_PHINE.PHINEFileService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/PHINEFile")
public class PHINEFileController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PHINEFileService phineFileService;

    /**
     * 保存文件
     * @param request
     * @param fileInfoVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveFileInfo", method = {RequestMethod.POST})
    public ApiResult saveFileUrl(HttpServletRequest request, @RequestBody FileInfoVo fileInfoVo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return phineFileService.saveFileInfo(tokenModel, fileInfoVo.getFilesInfo(), fileInfoVo.getProjectId());
    }

}
