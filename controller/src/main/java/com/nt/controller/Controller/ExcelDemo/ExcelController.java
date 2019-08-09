package com.nt.controller.Controller.ExcelDemo;

import com.nt.controller.Controller.ExcelDemo.demo.Employee;
import com.nt.controller.Controller.ExcelDemo.demo.Experience;
import com.nt.utils.ExcelOutPutUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel")
public class ExcelController {


    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public void save(HttpServletResponse response) throws Exception {
        Employee emp = getEmployee();
        List<Experience> educationList = getEducationList();
        List<Experience> workList = getWorkList();
        Map<String, Object> data = new HashMap<>();
        data.put("emp",emp);
        data.put("educationList",educationList);
        data.put("workList",workList);
        ExcelOutPutUtil.OutPut("123","employee.xlsx",data,response);
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
//        list.add(new Experience("广州科腾信息技术有限公司"));
        return list;
    }
}
