package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.dao_Pfans.PFANS2000.Vo.DutyfreeVo;
import com.nt.service_pfans.PFANS2000.DutyfreeService;
import com.nt.service_pfans.PFANS2000.mapper.BaseMapper;
import com.nt.service_pfans.PFANS2000.mapper.DutyfreeMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class DutyfreeServiceImpl implements  DutyfreeService {

    @Autowired
    private DutyfreeMapper dutyfreeMapper;

     @Autowired
    private BaseMapper baseMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<DutyfreeVo> getdutyfree(TokenModel tokenModel) throws Exception {
        List<DutyfreeVo> dutyfreeVolist = dutyfreeMapper.getdutyfree();
        return dutyfreeVolist;

    /*    List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            for (CustomerInfo customer : customerinfo) {

                Base base = new Base();
                base.setDepartment_id(customer.getUserinfo().getDepartmentid().toString());
                base.setUser_id(customer.getUserid());
                List<Base> baselist = baseMapper.select(base);
                if(baselist.size() > 0){
                    String user_id = baselist.get(0).getUser_id();
                    String department_id = baselist.get(0).getDepartment_id();
                }

            }
        }*/
    }


}
