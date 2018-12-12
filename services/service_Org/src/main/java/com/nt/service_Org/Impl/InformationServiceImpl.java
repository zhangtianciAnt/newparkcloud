package com.nt.service_Org.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Information;
import com.nt.service_Org.InformationService;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Information information,TokenModel tokenModel) throws Exception {
        if(information.getActivityinfo()!=null&&information.getActivityinfo().getSignupinfo()!=null)
        {
            information.getActivityinfo().preInsert(tokenModel);
            mongoTemplate.save(information);
        }
        else
        {
            if(information.getType().equals("8")||information.getType().equals("9")||information.getType().equals("10"))
            {
                Query query = new Query();
                query.addCriteria(Criteria.where("type").is(information.getType()));
                query.addCriteria(Criteria.where("releasestatus").is("1"));
                List<Information> rst = mongoTemplate.find(query,Information.class);
                if(rst.size() > 0){
                    rst.get(0).setReleasestatus("2");
                    mongoTemplate.save(rst.get(0));
                    mongoTemplate.save(information);
                }
                else
                {
                    mongoTemplate.save(information);
                }
            }
            else
            {
                mongoTemplate.save(information);
            }

        }
    }

    @Override
    public List<Information> get(Information information) throws Exception {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC,   "releasetime"));
        return mongoTemplate.find(query, Information.class);
    }

    //导出excel
    @Override
    public void importexcel (String id,HttpServletRequest request) throws Exception {

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        List<Information> rst = mongoTemplate.find(query,Information.class);

        if(rst.size() > 0){
            String title = rst.get(0).getTitle();
          List<Information.Signupinfo> signupinfoList =  rst.get(0).getActivityinfo().getSignupinfo();
          if(signupinfoList.size()>0)
          {
              ArrayList<Map<String, Object>> rows = CollUtil.newArrayList();
              for(int i=0;i<signupinfoList.size();i++)
              {
                  Map<String, Object> row = new LinkedHashMap<>();
                  row.put("标题",title);
                  row.put("公司名称", signupinfoList.get(i).getCompanyname());
                  row.put("姓名", signupinfoList.get(i).getName());
                  row.put("手机号", signupinfoList.get(i).getPhonenumber());
                  row.put("报名时间",signupinfoList.get(i).getCreateon());
                  rows.add(row);
              }


                //通过工具类创建writer
              String destFilePath = "D:/"+ title+"报名名单.xlsx";
              ExcelWriter writer = ExcelUtil.getWriter(destFilePath,"报名名单");

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

    //获取客户信息
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
    public List<Information> getInfoByType(String type) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(type));
        query.addCriteria(Criteria.where("releasestatus").is("1"));
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
}
