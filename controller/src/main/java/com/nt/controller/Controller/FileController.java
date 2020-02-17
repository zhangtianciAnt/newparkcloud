package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${file.url}")String Url;

    @RequestMapping(value = "/getToken",method={RequestMethod.GET})
    private ApiResult getToken() throws Exception {
        //无锡
//        String url = Url + "/?user/loginSubmit&isAjax=1&getToken=1&name=admin&password=newtouch1!";
        //大连
        String url = Url + "/kodexplorer/?user/loginSubmit&isAjax=1&getToken=1&name=admin&password=admin";

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> rst  = restTemplate.exchange(url, HttpMethod.GET,requestEntity,String.class);
        String value = rst.getBody();
        JSONObject string_to_json = JSONUtil.parseObj(value);
        return ApiResult.success(string_to_json);

    }
}

