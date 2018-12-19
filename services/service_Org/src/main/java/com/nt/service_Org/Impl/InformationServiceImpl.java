package com.nt.service_Org.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Information;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Org.InformationService;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @方法名：save
     * @描述：信息发布保存
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[information, request]
     * @返回值：
     */
    @Override
    public void save(Information information, TokenModel tokenModel) throws Exception {
        if (information.getActivityinfo() != null && information.getActivityinfo().getSignupinfo() != null) {
            information.getActivityinfo().preInsert(tokenModel);
            mongoTemplate.save(information);
        } else {
            if (information.getType().equals("8") || information.getType().equals("9") || information.getType().equals("10")) {
                if(information.getReleasestatus().equals("2"))
                {
                    mongoTemplate.save(information);
                }
                else
                {
                    Query query = new Query();
                    query.addCriteria(Criteria.where("type").is(information.getType()));
                    query.addCriteria(Criteria.where("releasestatus").is("1"));
                    List<Information> rst = mongoTemplate.find(query, Information.class);
                    if (rst.size() > 0) {
                        rst.get(0).setReleasestatus("2");
                        mongoTemplate.save(rst.get(0));
                        mongoTemplate.save(information);
                    } else {
                        mongoTemplate.save(information);
                    }
                }
            } else {
                mongoTemplate.save(information);
            }
        }
    }

    /**
     * @方法名：get
     * @描述：获取信息发布列表
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[information, request]
     * @返回值：Information
     */
    @Override
    public List<Information> get(Information information) throws Exception {
        //共同带权限查询
        Query query = CustmizeQuery(information);
        query.with(new Sort(Sort.Direction.DESC,   "releasetime"));
        if(information .getCurrentPage() != null && information.getPageSize() !=null){

            query.skip((information .getCurrentPage() - 1) * information.getPageSize());
            query.limit(information.getPageSize());
        }
        //优化查询速度
        if(information.getHttpOriginType() != null && information.getHttpOriginType().equals(AuthConstants.LOG_EQUIPMENT_PC)){

            query.fields().exclude("content");
            query.fields().exclude("imagespath");
        }
        return mongoTemplate.find(query, Information.class);

    }

    /**
     * @方法名：importexcel
     * @描述：导出excel到本地固定位置
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[id,request]
     * @返回值：
     */
    //导出excel
    @Override
    public void importexcel(String id, HttpServletRequest request) throws Exception {

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        List<Information> rst = mongoTemplate.find(query, Information.class);

        if (rst.size() > 0) {
            String title = rst.get(0).getTitle();
            List<Information.Signupinfo> signupinfoList = rst.get(0).getActivityinfo().getSignupinfo();
            if (signupinfoList.size() > 0) {
                ArrayList<Map<String, Object>> rows = CollUtil.newArrayList();
                for (int i = 0; i < signupinfoList.size(); i++) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("标题", title);
                    row.put("公司名称", signupinfoList.get(i).getCompanyname());
                    row.put("姓名", signupinfoList.get(i).getName());
                    row.put("手机号", signupinfoList.get(i).getPhonenumber());
                    row.put("报名时间", signupinfoList.get(i).getCreateon());
                    rows.add(row);
                }


                //通过工具类创建writer
                String destFilePath = "D:/" + title + "报名名单.xlsx";
                ExcelWriter writer = ExcelUtil.getWriter(destFilePath, "报名名单");

                //跳过当前行，既第一行，非必须，在此演示用
                //writer.passCurrentRow();

                //合并单元格后的标题行，使用默认标题样式
                //writer.merge(2, "测试标题");
                //一次性写出内容
                writer.write(rows);
                //关闭writer，释放内存
                writer.close();
            }
        }
    }

    /**
     * @方法名：getcustomerinfo
     * @描述：查询customerinfo
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[request]
     * @返回值：CustomerInfo
     */
    @Override
    public List<CustomerInfo> getcustomerinfo() throws Exception {
        Query query = new Query();
        //query.with(new Sort(Sort.Direction.DESC,   "releasetime"));
        return mongoTemplate.find(query, CustomerInfo.class);
    }

    /**
     * @方法名：getInfoByType
     * @描述：根据type获取发布信息
     * @创建日期：2018/12/11
     * @作者：ZHANGYING
     * @参数：[type, request]
     * @返回值：informationList
     */
    @Override
    public List<Information> getInfoByType(Information information) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(information.getType()));
        query.addCriteria(Criteria.where("releasestatus").is("1"));
        if(information .getCurrentPage() != null && information.getPageSize() !=null){

            query.skip((information .getCurrentPage() - 1) * information.getPageSize());
            query.limit(information.getPageSize());
        }
        query.with(new Sort(Sort.Direction.DESC, "releasetime"));
        List<Information> informationList = mongoTemplate.find(query, Information.class);
        return informationList;
    }

    /**
     * @方法名：getInfoById
     * @描述：根据id获取发布信息
     * @创建日期：2018/12/11
     * @作者：ZHANGYING
     * @参数：[id, request]
     * @返回值：information
     */
    @Override
    public Information getInfoById(String id) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Information information = mongoTemplate.findOne(query, Information.class);
        return information;
    }

    /**
     * @方法名：addActivity
     * @描述：报名成功添加用户信息
     * @创建日期：2018/12/11
     * @作者：ZHANGYING
     * @参数：[id, request]
     * @返回值：information
     */
    @Override
    public void addActivity(Information information, String id) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        UserAccount userAccount = mongoTemplate.findOne(query, UserAccount.class);
        if(userAccount != null) {
            Query queryCus = new Query();
            queryCus.addCriteria(Criteria.where("userid").is(id));
            CustomerInfo customerInfo = mongoTemplate.findOne(queryCus, CustomerInfo.class);
            if(customerInfo != null) {
                if(customerInfo.getUserinfo() != null) {
                    List<Information.Signupinfo> signupinfos = new ArrayList<Information.Signupinfo>();
                    Information.Signupinfo s = new Information.Signupinfo();
                    s.set_id(id);
                    s.setCompanyname(customerInfo.getUserinfo().getCompanyname());
                    s.setPhonenumber(customerInfo.getUserinfo().getMobilenumber());
                    s.setName(customerInfo.getUserinfo().getCustomername());
                    signupinfos.add(s);
                    if(information.getActivityinfo().getSignupinfo() != null) {
                        information.getActivityinfo().getSignupinfo().addAll(signupinfos);
                    }else {
                        information.getActivityinfo().setSignupinfo(signupinfos);
                    }

                    mongoTemplate.save(information);
                }
            }
        }
    }
}
