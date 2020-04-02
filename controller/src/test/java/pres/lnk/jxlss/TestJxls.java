package pres.lnk.jxlss;

import cn.hutool.core.convert.Convert;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mongodb.MongoClient;
import com.nt.controller.Start;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.utils.AuthConstants;
import com.nt.utils.ExcelOutPutUtil;
import com.nt.utils.jxlsUtil.JxlsBuilder;
import com.nt.utils.jxlsUtil.JxlsImage;
import com.nt.utils.jxlsUtil.JxlsUtil;
import com.nt.utils.services.TokenService;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    MongoTemplate template;

    private static Logger log = LoggerFactory.getLogger(TestJxls.class);

    @Before
    public void init() {
        MongoDbFactory facotry = new SimpleMongoDbFactory(new MongoClient("39.108.133.62", 27017), "PFANS");
        template = new MongoTemplate(facotry);
    }

    @Test
    public void testJxls() throws Exception {
//        Employee emp = getEmployee();
//        List<Experience> educationList = getEducationList();
//        List<Experience> workList = getWorkList();
//        Map<String, Object> data = new HashMap<>();
//        data.put("emp",emp);
//        data.put("educationList",educationList);
//        data.put("workList",workList);
        //ExcelOutPutUtil.OutPut("123","employee.xlsx",data,response);
//        List<CustomerInfo> rst = new ArrayList<CustomerInfo>();
//        ExcelReader reader = ExcelUtil.getReader("d:/导入用户管理.xlsx");
//        List<Map<String,Object>> readAll = reader.readAll();
//        String i = "" ;
//        for(Map<String,Object> item :readAll){
//                        Query query = new Query();
//            query.addCriteria(Criteria.where("userinfo.customername").is(item.get("氏名")));
//            List<CustomerInfo> customerInfos = template.find(query, CustomerInfo.class);
//            if(customerInfos.size() > 0){
//                CustomerInfo customerInfo = customerInfos.get(0);
//                customerInfo.getUserinfo().setSex(Convert.toStr(item.get("性别")));
//                customerInfo.getUserinfo().setBirthday(Convert.toStr(item.get("生年月日")));
//                customerInfo.getUserinfo().setNationality(Convert.toStr(item.get("国籍")));
//                customerInfo.getUserinfo().setNation(Convert.toStr(item.get("民族")));
//                customerInfo.getUserinfo().setIdnumber(Convert.toStr(item.get("身份証番号")));
//                customerInfo.getUserinfo().setSecurity(Convert.toStr(item.get("社会保険番号")));
//                customerInfo.getUserinfo().setHousefund(Convert.toStr(item.get("住宅積立金番号")));
//                customerInfo.getUserinfo().setMarital(Convert.toStr(item.get("婚姻状况")));
//                customerInfo.getUserinfo().setChildren(Convert.toStr(item.get("独生子")));
//                customerInfo.getUserinfo().setDifference(Convert.toStr(item.get("仕事経験有無")));
//                customerInfo.getUserinfo().setGraduation(Convert.toStr(item.get("最终毕业学校")));
//                customerInfo.getUserinfo().setDegree(Convert.toStr(item.get("最終学位")));
//                customerInfo.getUserinfo().setEducational(Convert.toStr(item.get("最終学歷")));
//                customerInfo.getUserinfo().setSpecialty(Convert.toStr(item.get("専攻")));
//                customerInfo.getUserinfo().setGraduationday(Convert.toStr(item.get("卒業年月日")));
//                customerInfo.getUserinfo().setWorkday(Convert.toStr(item.get("仕事开始年月日")));
//                customerInfo.getUserinfo().setTeamname(Convert.toStr(item.get("チーム")));
//                customerInfo.getUserinfo().setGroupname(Convert.toStr(item.get("グループ")));
//                customerInfo.getUserinfo().setCentername(Convert.toStr(item.get("センター")));
//                customerInfo.getUserinfo().setBudgetunit(Convert.toStr(item.get("预算单位")));
//                customerInfo.getUserinfo().setPersonalcode(Convert.toStr(item.get("个人编码")));
//                customerInfo.getUserinfo().setPost(Convert.toStr(item.get("職務")));
//                customerInfo.getUserinfo().setType(Convert.toStr(item.get("类别")));
//                customerInfo.getUserinfo().setRank(Convert.toStr(item.get("ランク")));
//                customerInfo.getUserinfo().setEnterday(Convert.toStr(item.get("入社年月日")));
//                customerInfo.getUserinfo().setTeamid(Convert.toStr(item.get("チームID")));
//                customerInfo.getUserinfo().setGroupid(Convert.toStr(item.get("グループID")));
//                customerInfo.getUserinfo().setCenterid(Convert.toStr(item.get("センターID")));
//
//                template.save(customerInfo);
//            }
//        }

//            Query query = new Query();
//            query.addCriteria(Criteria.where("_id").is(item.get("id")));
//            List<CustomerInfo> customerInfos = template.find(query, CustomerInfo.class);
//
//            if(customerInfos.size() > 0){
//                customerInfos.get(0).getUserinfo().setCenterid(item.get("ci").toString());
//                customerInfos.get(0).getUserinfo().setGroupid(item.get("gi").toString());
//                customerInfos.get(0).getUserinfo().setTeamid(item.get("ti").toString());
//                customerInfos.get(0).getUserinfo().setCentername(item.get("cn").toString());
//                customerInfos.get(0).getUserinfo().setGroupname(item.get("gn").toString());
//                customerInfos.get(0).getUserinfo().setTeamname(item.get("tn").toString());
//                template.save(customerInfos.get(0));
//            }
//
//        }

//        ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
//        for(CustomerInfo item:rst){
//            Map<String, Object> row1 = new LinkedHashMap<>();
//            row1.put("id", item.get_id());
//            row1.put("name", item.getUserinfo().getCustomername());
//            rows.add(row1);
//        }
//        ExcelWriter writer = ExcelUtil.getWriter("d:/1111.xlsx");
//        writer.write(rows, true);
//        writer.close();

//        Query query = new Query();
//        query.addCriteria(Criteria.where("usertype").ne("1"));
//        List<UserAccount> list = template.find(query, UserAccount.class);
//
//        query = new Query();
//        query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
//        query.addCriteria(Criteria.where("rolename").is("正式社员"));
//        List<Role>  rolss =  template.find(query, Role.class);
//
//        for(UserAccount item:list){
//            item.setRoles(rolss);
//            template.save(item);
//        }
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
