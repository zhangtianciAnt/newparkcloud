package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.dao_BASF.Emergencyplan;
import com.nt.dao_BASF.VO.EmergencyplanVo;
import com.nt.service_BASF.EmergencyplanServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10802Controller
 * @Author: Y
 * @Description: BASF接警单管理模块Controller
 * @Date: 2019/11/18 18：03
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10802")
public class BASF10802Controller {
    @Autowired
    private EmergencyplanServices emergencyplanServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Y
     * @Version 1.0
     * @Description 获取应急预案列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18：03
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(emergencyplanServices.list());
    }

    @Autowired
    private RestTemplate restTemplate;
    @Value("${file.url}")String Url;
    /**
     * @param emergencyplanvo
     * @param request
     * @Method create
     * @Author Y
     * @Version 1.0
     * @Description 创建应急预案
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:03
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody EmergencyplanVo emergencyplanvo, HttpServletRequest request) throws Exception {
        if (emergencyplanvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        Emergencyplan  emergencyplan = emergencyplanvo.getEmergencyplan();

        String url = Url + "/kodexplorer/?explorer/pathInfo&accessToken="+emergencyplanvo.getToken();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String path = emergencyplan.getUploadfile().split(",")[1];
        map.add("dataArr", "[{\"type\":\"file\",\"path\":\""+path.substring(0,path.length()-1)+"\"}]");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> rst  = restTemplate.exchange(url, HttpMethod.POST,requestEntity,String.class);
        String value = rst.getBody();
        JSONObject string_to_json = JSONUtil.parseObj(value);
        Object data = string_to_json.get("data");
        String downloadPath = ((JSONObject) data).getStr("downloadPath");
        emergencyplan.setDownloadpath(downloadPath);
        emergencyplanServices.insert(emergencyplan, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param emergencyplan
     * @param request
     * @Method delete
     * @Author Y
     * @Version 1.0
     * @Description 删除应急预案
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18：02
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Emergencyplan emergencyplan, HttpServletRequest request) throws Exception {
        if (emergencyplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        emergencyplan.setStatus(AuthConstants.DEL_FLAG_DELETE);
        emergencyplanServices.delete(emergencyplan);
        return ApiResult.success();
    }

    /**
     * @param emergencyplanid
     * @param request
     * @Method selectById
     * @Author Y
     * @Version 1.0
     * @Description 获取应急预案详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:01
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String emergencyplanid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(emergencyplanid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(emergencyplanServices.one(emergencyplanid));
    }

    /**
     * @param emergencyplanvo
     * @param request
     * @Method update
     * @Author Y
     * @Version 1.0
     * @Description 更新应急预案详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:01
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody EmergencyplanVo emergencyplanvo, HttpServletRequest request) throws Exception {
        if (emergencyplanvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        Emergencyplan  emergencyplan = emergencyplanvo.getEmergencyplan();

        String url = Url + "/kodexplorer/?explorer/pathInfo&accessToken="+ emergencyplanvo.getToken();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String path = emergencyplan.getUploadfile().split(",")[1];
        map.add("dataArr", "[{\"type\":\"file\",\"path\":\""+path.substring(0,path.length()-1)+"\"}]");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> rst  = restTemplate.exchange(url, HttpMethod.POST,requestEntity,String.class);
        String value = rst.getBody();
        JSONObject string_to_json = JSONUtil.parseObj(value);
        Object data = string_to_json.get("data");
        String downloadPath = ((JSONObject) data).getStr("downloadPath");
        emergencyplan.setDownloadpath(downloadPath);

        emergencyplanServices.update(emergencyplan, tokenModel);
        return ApiResult.success();
    }
}
