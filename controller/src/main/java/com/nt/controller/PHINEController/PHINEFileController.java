package com.nt.controller.PHINEController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Filemark2file;
import com.nt.dao_PHINE.Vo.FileInfoVo;
import com.nt.service_PHINE.PHINEFileService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/PHINEFile")
public class PHINEFileController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PHINEFileService phineFileService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/uploadFile", method = {RequestMethod.POST})
    public ApiResult uploadFile(HttpServletRequest request, @RequestParam String accessToken, @RequestBody MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            // 文件名
            String fileName = file.getOriginalFilename();
            // 临时文件目录
            String tempFilePath = System.getProperty("java.io.tmpdir") + file.getOriginalFilename();
            //生成临时文件
            File tempFile = new File(tempFilePath);
            file.transferTo(tempFile);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));

            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            //MultipartFile 直接转 fileSystemResource 是不行的, 需要把临时文件变成filesystemresource
            FileSystemResource resource = new FileSystemResource(tempFilePath);
            param.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);
            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(accessToken, requestEntity, JSONObject.class);
            JSONObject jsonObject = responseEntity.getBody();

            //删除临时文件文件
            tempFile.delete();
            return ApiResult.success(jsonObject);
        }
        return null;
    }

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
