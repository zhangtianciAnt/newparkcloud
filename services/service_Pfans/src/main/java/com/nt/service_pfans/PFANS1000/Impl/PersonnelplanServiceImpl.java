package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ExternalVo;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS1000.PersonnelplanService;
import com.nt.service_pfans.PFANS1000.mapper.PersonnelplanMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PersonnelplanServiceImpl implements PersonnelplanService {

    @Autowired
    private PersonnelplanMapper personnelplanMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<CustomerInfo> SelectCustomer(String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("userinfo.groupid").is(id);
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        return customerInfos;
    }

    @Override
    public List<Supplierinfor> getExternal() {
         List<Supplierinfor> supplierinfors = personnelplanMapper.getSupplierinfor();
        return supplierinfors;
    }

    @Override
    public List<ExternalVo> getExpatriatesinfor(String groupid) {
        List<ExternalVo> externalVos = personnelplanMapper.getExternal(groupid);
        return externalVos;
    }

    @Override
    public List<PersonnelPlan> getAll() {
        PersonnelPlan personnelPlan = new PersonnelPlan();
        List<PersonnelPlan> personnelPlans = personnelplanMapper.selectAll();
        return personnelPlans;
    }

    @Override
    public PersonnelPlan getOne(String id) {
        PersonnelPlan personnelPlan =  personnelplanMapper.selectByPrimaryKey(id);
        return personnelPlan;
    }

    @Override
    public void update(PersonnelPlan personnelPlan, TokenModel tokenModel) {
        personnelPlan.preUpdate(tokenModel);
        personnelplanMapper.updateByPrimaryKeySelective(personnelPlan);
    }

    @Override
    public void insert(PersonnelPlan personnelPlan, TokenModel tokenModel) {
        personnelPlan.preInsert(tokenModel);
        personnelPlan.setPersonnelplanid(UUID.randomUUID().toString());
        personnelplanMapper.insert(personnelPlan);
    }

    @Override
    public List<PersonnelPlan> get(PersonnelPlan personnelPlan) {
        personnelPlan.setStatus("0");
      List<PersonnelPlan> personnelPlan1 =  personnelplanMapper.select(personnelPlan);
      personnelPlan.setType(1);
      if(personnelplanMapper.select(personnelPlan).size() > 0) {
          personnelPlan1.addAll(personnelplanMapper.select(personnelPlan));
      }
      return  personnelPlan1;
    }
}
