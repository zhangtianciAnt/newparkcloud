package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.dao_Pfans.PFANS2000.Contrast;
import com.nt.service_pfans.PFANS2000.ContrastService;
import com.nt.service_pfans.PFANS2000.mapper.BaseMapper;
import com.nt.service_pfans.PFANS2000.mapper.ContrastMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ContrastServiceImpl implements ContrastService {

    @Autowired
    private ContrastMapper contrastMapper;

     @Autowired
    private BaseMapper baseMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Override
//    public void insert(TokenModel tokenModel) throws Exception {
//        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
//        if (customerinfo != null) {
//            for (CustomerInfo customer : customerinfo) {
//
//                Base base = new Base();
//                base.setDepartment_id(customer.getUserinfo().getDepartmentid().toString());
//                base.setUser_id(customer.getUserid());
//                List<Base> baselist = baseMapper.select(base);
//                if(baselist.size() > 0){
//                    String user_id = baselist.get(0).getUser_id();
//                    String department_id = baselist.get(0).getDepartment_id();
//                }
//                Contrast contrast = new Contrast();
//                String consrastid = UUID.randomUUID().toString();
//                contrast.preInsert(tokenModel);
//                contrast.setContrast_id(consrastid);
//                contrast.setUser_id(customer.getUserid());
//                contrast.setOwner(customer.getUserid());
//                contrast.setDepartment_id(customer.getUserinfo().getDepartmentid().toString());
//
//                contrastMapper.insertSelective(contrast);
//            }
//        }
//    }

    @Override
    public List<Contrast> getList(Contrast contrast) throws Exception {
        return contrastMapper.select(contrast);
    }

}
