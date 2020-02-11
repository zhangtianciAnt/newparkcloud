package com.nt.controller.PHINEController;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Filemark2file;
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
     *
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

    /**
     * 根据项目id获取文件列表
     *
     * @param request
     * @param projectId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getFileByProjectId", method = {RequestMethod.GET})
    public ApiResult getFileByProjectId(HttpServletRequest request, @RequestParam String projectId) throws Exception {
        return phineFileService.getFileByProjectId(projectId);
    }

    /**
     * 根据项目id获取文件版本
     *
     * @param request
     * @param projectId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getFileMarkByProjectId", method = {RequestMethod.GET})
    public ApiResult getFileMarkByProjectId(HttpServletRequest request, @RequestParam String projectId) throws Exception {
        return phineFileService.getFileMarkByProjectId(projectId);
    }

    /**
     * 根据版本id获取相关文件
     *
     * @param request
     * @param projectId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getFilesByFileMarkId", method = {RequestMethod.GET})
    public ApiResult getFilesByFileMarkId(HttpServletRequest request, @RequestParam String projectId) throws Exception {
        return phineFileService.getFilesByFileMarkId(projectId);
    }

    /**
     * 根据文件id更新文件
     *
     * @param request
     * @param fileinfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateFileNameById", method = {RequestMethod.POST})
    ApiResult updateFileNameById(HttpServletRequest request, @RequestBody Fileinfo fileinfo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return phineFileService.updateFileNameById(tokenModel, fileinfo);
    }

    /**
     * 根据FileMarkId和fileid删除关系表
     *
     * @param request
     * @param filemark2fileList
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delFileMark2File", method = {RequestMethod.POST})
    ApiResult delFileMark2File(HttpServletRequest request, @RequestBody List<Filemark2file> filemark2fileList) throws Exception {
        return phineFileService.delFileMark2File(filemark2fileList);
    }

}
