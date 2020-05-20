package com.nt.controller.Controller.ExcelDemo;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nt.controller.Controller.ExcelDemo.demo.Employee;
import com.nt.controller.Controller.ExcelDemo.demo.Experience;
import com.nt.dao_Org.CustomerInfo;
import com.nt.utils.ExcelOutPutUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/excel")
public class ExcelController {


    @Autowired
    private MongoTemplate mongoTemplate;

    //ADD_FJL_05/19   --人员code转name
    @RequestMapping(value = "/excel1", method = {RequestMethod.POST})
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void save1(HttpServletResponse response, HttpServletRequest request) throws Exception {
        ExcelReader reader = ExcelUtil.getReader("e:/testqindai.xlsx");
        ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> readAll = reader.readAll();
        for (Map<String, Object> item : readAll) {
            Query query = new Query();
            query.addCriteria(Criteria.where("userid").is(item.get("人名")));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("人名", customerInfo.getUserinfo().getCustomername());
            row1.put("日期", item.get("日期"));
            row1.put("事假", item.get("事假"));
            row1.put("产休/护理假", item.get("产休/护理假"));
            row1.put("欠勤", item.get("欠勤"));
            row1.put("短病假", item.get("短病假"));
            row1.put("长病假", item.get("长病假"));
            row1.put("平日", item.get("平日"));
            row1.put("休日", item.get("休日"));
            row1.put("祝日", item.get("祝日"));
            row1.put("特别休日", item.get("特别休日"));
            row1.put("一齐年休", item.get("一齐年休"));
            row1.put("青年节", item.get("青年节"));
            row1.put("妇女节", item.get("妇女节"));
            rows.add(row1);
        }
        ExcelWriter writer = ExcelUtil.getWriter("e:/testqindai.xlsx");
        writer.write(rows, true);
        writer.close();
    }

    //ADD_FJL_05/19   --人员code转name
    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public void save(HttpServletResponse response) throws Exception {
        Employee emp = getEmployee();
        List<Experience> educationList = getEducationList();
        List<Experience> workList = getWorkList();
        Map<String, Object> data = new HashMap<>();
        data.put("emp", emp);
        data.put("educationList", educationList);
        data.put("workList", workList);
        ExcelOutPutUtil.OutPut("123", "jiejipai.xlsx", data, response);

//        Map<String, Object> data = new HashMap<>();
//        Employee emp = getEmployee();
//        emp.setName("测试公司");
//        data.put("emp",emp);
//        ExcelOutPutUtil.OutPut("222","test.xlsx",data,response);
    }

    private static Employee getEmployee() {
        return new Employee();
    }

    private static List<Experience> getEducationList() {
        List<Experience> list = new ArrayList<>();
        list.add(new Experience("xxx大学"));
        list.add(new Experience("xxx高中"));
        list.add(new Experience("xxx初中"));
        return list;
    }


    private static List<Experience> getWorkList() {
        List<Experience> list = new ArrayList<>();
        list.add(new Experience("广州科腾信息技术有限公司"));
        return list;
    }
}
