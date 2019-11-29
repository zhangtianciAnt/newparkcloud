package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/upload",method={RequestMethod.POST})
    public ApiResult upload(HttpServletRequest request) throws Exception {


        return ApiResult.success();
    }

    @RequestMapping(value = "/download",method={RequestMethod.GET})
    public ApiResult download(String path,HttpServletRequest request) throws Exception {
        String token = getToken();

        return ApiResult.success();
    }

    private String getToken() throws Exception {
        String url = "http://39.108.133.62:8002/kodexplorer/?user/loginSubmit&isAjax=1&getToken=1&name=admin&password=admin";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<Object> rst  = restTemplate.exchange(url, HttpMethod.GET,requestEntity,Object.class);
//        String value = rst.getBody();
        String value="";
        return value;

    }
}

