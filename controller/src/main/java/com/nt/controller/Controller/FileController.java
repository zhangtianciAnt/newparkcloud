package com.nt.controller.Controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${file.url}")String Url;

    @RequestMapping(value = "/getToken",method={RequestMethod.GET})
    private ApiResult getToken() throws Exception {
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

