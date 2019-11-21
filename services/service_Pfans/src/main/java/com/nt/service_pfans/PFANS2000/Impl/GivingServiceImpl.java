package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.dao_Pfans.PFANS2000.Giving;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.mapper.BaseMapper;
import com.nt.service_pfans.PFANS2000.mapper.GivingMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class GivingServiceImpl implements GivingService {

    @Autowired
    private GivingMapper givingMapper;

    @Autowired
    private BaseMapper baseMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 生成基数表
     * FJL
     * */
    @Override
    public void insertBase(TokenModel tokenModel) throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            for (CustomerInfo customer : customerinfo) {

                Base base = new Base();
                String baseid = UUID.randomUUID().toString();
                base.preInsert(tokenModel);
                base.setBase_id(baseid);
                base.setUser_id(customer.getUserid());  //名字
                base.setOwner(customer.getUserid());
                base.setDepartment_id(customer.getUserinfo().getDepartmentid().toString());  //部门
//              base.setRn(customer.get);  //RN
                base.setSex(customer.getUserinfo().getSex());  //性别
//                base.setOnlychild(customer.get);  //独生子女
//                base.setBonus(customer);  //奨金計上
                base.setAccumulation(customer.getUserinfo().getHousefund());  //公积金基数
//                base.setWorkdate(customer.getUserinfo().getEnterday().format('YYYY-MM-DD'));  //入社日
                baseMapper.insertSelective(base);
            }
        }
    }

    @Override
    public List<Base> getListtBase(Base base) throws Exception{
        return baseMapper.select(base);
    }

    @Override
    public void insert(Giving giving, TokenModel tokenModel) throws Exception {
        giving.preInsert(tokenModel);
        giving.setGiving_id(UUID.randomUUID().toString());
        givingMapper.insert(giving);
    }

    @Override
    public List<Giving> getDataList(Giving giving) throws Exception {

        return givingMapper.select(giving);
    }
}
