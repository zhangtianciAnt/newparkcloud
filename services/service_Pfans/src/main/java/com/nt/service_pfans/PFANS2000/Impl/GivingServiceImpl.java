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

import java.util.Date;
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
    public void insertBase(String givingid,TokenModel tokenModel) throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            int rowindex = 0;
            for (CustomerInfo customer : customerinfo) {
                rowindex = rowindex + 1;
                Base base = new Base();
                String baseid = UUID.randomUUID().toString();
                base.preInsert(tokenModel);
                base.setBase_id(baseid);
                base.setGiving_id(givingid);
                base.setUser_id(customer.getUserid());  //名字
                base.setOwner(customer.getUserid());
                base.setDepartment_id(customer.getUserinfo().getDepartmentid().toString());  //部门
//              base.setRn(customer.get);  //RN
                base.setSex(customer.getUserinfo().getSex());  //性别
//                base.setOnlychild(customer.getUserinfo().getChildren());  //独生子女
                //入/退職/産休
//                base.setBonus(customer);  //奨金計上
                //1999年前社会人
//                base.setRegistered(customer.getUserinfo().getNationality()); //大連戸籍
                //2019年6月
                //2019年7月
                base.setPension(customer.getUserinfo().getOldageinsurance()); //養老・失業・工傷基数
                base.setMedical(customer.getUserinfo().getMedicalinsurance()); //医療・生育基数

                base.setAccumulation(customer.getUserinfo().getHousefund());  //公积金基数
                //采暖费
//                base.setWorkdate(customer.getUserinfo().getEnterday().format('YYYY-MM-DD'));  //入社日
                base.setRowindex(rowindex);
                baseMapper.insertSelective(base);
            }
        }
    }

    @Override
    public List<Base> getListtBase(Base base) throws Exception{
        return baseMapper.select(base);
    }

    @Override
    public void insert(String generation, TokenModel tokenModel) throws Exception {
        String givingid =  UUID.randomUUID().toString();
        Giving giving = new Giving();
        giving.preInsert(tokenModel);
        giving.setGiving_id(givingid);
        giving.setGeneration(generation);
        giving.setGenerationdate(new Date());
        givingMapper.insert(giving);
        insertBase(givingid,tokenModel);
        insertBase(givingid,tokenModel);
        insertBase(givingid,tokenModel);
        insertBase(givingid,tokenModel);
        insertBase(givingid,tokenModel);
        insertBase(givingid,tokenModel);
        insertBase(givingid,tokenModel);
    }

    @Override
    public List<Giving> getDataList(Giving giving) throws Exception {

        return givingMapper.select(giving);
    }
}
