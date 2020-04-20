package com.nt.controller.Controller.Tools;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userTools")
public class UserToolsController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/importUser", method = {RequestMethod.POST})
    public void importUser(HttpServletRequest request) throws Exception {
        List<String> Result = new ArrayList<String>();
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        File f = null;
        f = File.createTempFile("tmp", null);
        file.transferTo(f);
        ExcelReader reader = ExcelUtil.getReader(f);
        List<Map<String, Object>> readAll = reader.readAll();
        for (Map<String, Object> item : readAll) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(item.get("id")));
            List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
            if(customerInfos.size() > 0) {
                customerInfos.get(0).getUserinfo().setCaiwupersonalcode(item.get("code").toString());
                mongoTemplate.save(customerInfos.get(0));
            }
        }
    }
}
