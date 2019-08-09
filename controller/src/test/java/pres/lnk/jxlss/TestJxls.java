package pres.lnk.jxlss;

import com.nt.utils.ExcelOutPutUtil;
import com.nt.utils.jxlsUtil.JxlsBuilder;
import com.nt.utils.jxlsUtil.JxlsImage;
import com.nt.utils.jxlsUtil.JxlsUtil;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Before;
import org.junit.Test;
import pres.lnk.jxlss.demo.Employee;
import pres.lnk.jxlss.demo.Experience;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 测试生成excel文件
 *
 * @Author lnk
 * @Date 2018/1/25
 */
public class TestJxls {

    @Test
    public void testJxls() throws Exception {
        Employee emp = getEmployee();
        List<Experience> educationList = getEducationList();
        List<Experience> workList = getWorkList();
        Map<String, Object> data = new HashMap<>();
        data.put("emp",emp);
        data.put("educationList",educationList);
        data.put("workList",workList);
        //ExcelOutPutUtil.OutPut("123","employee.xlsx",data,response);
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
