package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.dao_BASF.Chemicalsds;
import com.nt.dao_BASF.VO.ChemicalsdsVo;
import com.nt.service_BASF.ChemicalsdsServices;
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
 * @ClassName: BASF10804Controller
 * @Author: Y
 * @Description: BASF化学品SDS模块Controller
 * @Date: 2019/11/18 18：03
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10804")
public class BASF10804Controller {
    @Autowired
    private ChemicalsdsServices chemicalsdsServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Y
     * @Version 1.0
     * @Description 获取化学品SDS列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18：03
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(chemicalsdsServices.list());
    }

    @Autowired
    private RestTemplate restTemplate;
    @Value("${file.url}")String Url;
    /**
     * @param chemicalsdsVo
     * @param request
     * @Method create
     * @Author Y
     * @Version 1.0
     * @Description 创建化学品SDS
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:03
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody ChemicalsdsVo chemicalsdsVo, HttpServletRequest request) throws Exception {
        if (chemicalsdsVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        Chemicalsds chemicalsds = chemicalsdsVo.getChemicalsds();

        if (chemicalsds.getUploadfile() != null && !"".equals(chemicalsds.getUploadfile())) {
            String url = Url + "/kodexplorer/?explorer/pathInfo&accessToken=" + chemicalsdsVo.getToken();
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            String path = chemicalsds.getUploadfile().split(",")[1];
            map.add("dataArr", "[{\"type\":\"file\",\"path\":\"" + path.substring(0, path.length() - 1) + "\"}]");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
            ResponseEntity<String> rst = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String value = rst.getBody();
            JSONObject string_to_json = JSONUtil.parseObj(value);
            Object data = string_to_json.get("data");
            String downloadPath = ((JSONObject) data).getStr("downloadPath");
            chemicalsds.setDownloadpath(downloadPath);
        }
        chemicalsdsServices.insert(chemicalsds, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param chemicalsds
     * @param request
     * @Method delete
     * @Author Y
     * @Version 1.0
     * @Description 删除化学品SDS
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18：02
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Chemicalsds chemicalsds, HttpServletRequest request) throws Exception {
        if (chemicalsds == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        chemicalsds.setStatus(AuthConstants.DEL_FLAG_DELETE);
        chemicalsdsServices.delete(chemicalsds);
        return ApiResult.success();
    }

    /**
     * @param chemicalsdsid
     * @param request
     * @Method selectById
     * @Author Y
     * @Version 1.0
     * @Description 获取化学品SDS详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:01
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String chemicalsdsid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(chemicalsdsid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(chemicalsdsServices.one(chemicalsdsid));
    }

    /**
     * @param chemicalsdsVo
     * @param request
     * @Method update
     * @Author Y
     * @Version 1.0
     * @Description 更新化学品SDS详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:01
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody ChemicalsdsVo chemicalsdsVo, HttpServletRequest request) throws Exception {
        if (chemicalsdsVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        Chemicalsds chemicalsds = chemicalsdsVo.getChemicalsds();

        if (chemicalsds.getUploadfile() != null && !"".equals(chemicalsds.getUploadfile())){

            String url = Url + "/kodexplorer/?explorer/pathInfo&accessToken="+ chemicalsdsVo.getToken();
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            String path = chemicalsds.getUploadfile().split(",")[1];
            map.add("dataArr", "[{\"type\":\"file\",\"path\":\""+path.substring(0,path.length()-1)+"\"}]");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
            ResponseEntity<String> rst  = restTemplate.exchange(url, HttpMethod.POST,requestEntity,String.class);
            String value = rst.getBody();
            JSONObject string_to_json = JSONUtil.parseObj(value);
            Object data = string_to_json.get("data");
            String downloadPath = ((JSONObject) data).getStr("downloadPath");
            chemicalsds.setDownloadpath(downloadPath);
        }

        chemicalsdsServices.update(chemicalsds, tokenModel);
        return ApiResult.success();
    }
}
