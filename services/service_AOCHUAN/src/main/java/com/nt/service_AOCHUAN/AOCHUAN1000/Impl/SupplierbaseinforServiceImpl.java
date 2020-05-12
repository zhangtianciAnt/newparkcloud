package com.nt.service_AOCHUAN.AOCHUAN1000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.dao_Org.CustomerInfo;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierbaseinforMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SupplierbaseinforServiceImpl implements SupplierbaseinforService {
    @Autowired
    private SupplierbaseinforMapper supplierbaseinforMapper;


    @Override
    public List<Supplierbaseinfor> get() throws Exception {
        return supplierbaseinforMapper.selectAll();
    }

    @Override
    public Supplierbaseinfor getOne(String id) throws Exception {
        return supplierbaseinforMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Supplierbaseinfor supplierbaseinfor, TokenModel tokenModel) throws Exception {
        supplierbaseinfor.preUpdate(tokenModel);
        supplierbaseinforMapper.updateByPrimaryKeySelective(supplierbaseinfor);
    }

    @Override
    public String insert(Supplierbaseinfor supplierbaseinfor, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        supplierbaseinfor.setSupplierbaseinfor_id(id);
        supplierbaseinfor.preInsert(tokenModel);
        supplierbaseinforMapper.insert(supplierbaseinfor);
        return id;
    }

    @Override
    public void delete(String id) throws Exception {
        supplierbaseinforMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Supplierbaseinfor> getSuppliersExceptUnique() throws Exception {
        return supplierbaseinforMapper.getSuppliersExceptUnique();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importInfo() throws Exception {
        /*try {
           *//* List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);*//*
            ExcelReader reader = ExcelUtil.getReader(f);
            List<Map<String, Object>> readAll = reader.readAll();
            int accesscount = 0;
            int error = 0;
            for (Map<String, Object> item : readAll) {
                *//*Query query = new Query();
                query.addCriteria(Criteria.where("userid").is(item.get("社員ID")));
                List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
                *//*
                if (customerInfos.size() > 0) {
                    customerInfos.get(0).getUserinfo().setCenterid(item.get("centerid").toString());
                    customerInfos.get(0).getUserinfo().setGroupid(item.get("groupid").toString());
                    customerInfos.get(0).getUserinfo().setTeamid(item.get("teamid").toString());
                    customerInfos.get(0).getUserinfo().setCentername(item.get("所属センター").toString());
                    customerInfos.get(0).getUserinfo().setGroupname(item.get("所属グループ").toString());
                    customerInfos.get(0).getUserinfo().setTeamname(item.get("所属チーム").toString());
                    customerInfos.get(0).getUserinfo().setBudgetunit(item.get("予算単位").toString());
                    customerInfos.get(0).getUserinfo().setPost(item.get("職務").toString());
                    customerInfos.get(0).getUserinfo().setRank(item.get("ランク").toString());
                    customerInfos.get(0).getUserinfo().setLaborcontractday(item.get("労働契約締切日").toString());
                    customerInfos.get(0).getUserinfo().setAnnuallastyear(item.get("去年年休数(残)").toString());
                    customerInfos.get(0).getUserinfo().setAnnualyear(item.get("今年年休数").toString());
                    customerInfos.get(0).getUserinfo().setUpgraded(item.get("昇格昇号年月日").toString());
                    customerInfos.get(0).getUserinfo().setSeatnumber(item.get("口座番号").toString());
                    List<CustomerInfo.Personal> cupList = new ArrayList<CustomerInfo.Personal>();
                    CustomerInfo.Personal personal = new CustomerInfo.Personal();
                    personal.setAfter(item.get("変更前基本工资").toString());
                    personal.setBefore(item.get("変更前职责工资").toString());
                    personal.setBasic(item.get("変更后基本工资").toString());
                    personal.setDuty(item.get("変更后职责工资").toString());
                    personal.setDate(item.get("給料変更日").toString());
                    cupList.add(personal);
                    customerInfos.get(0).getUserinfo().setGridData(cupList);
                    customerInfos.get(0).getUserinfo().setOldageinsurance(item.get("養老保険基数").toString());
                    customerInfos.get(0).getUserinfo().setMedicalinsurance(item.get("医療保険基数").toString());
                    customerInfos.get(0).getUserinfo().setHouseinsurance(item.get("住宅積立金納付基数").toString());
                    mongoTemplate.save(customerInfos.get(0));
                }

                Supplierbaseinfor supplierbaseinfor = new Supplierbaseinfor();
                supplierbaseinfor.setSupplierbaseinfor_id(UUID.randomUUID().toString());
                supplierbaseinfor.setSuppliernamecn(item.get("供应商中文名").toString());
                supplierbaseinfor.setSuppliernameen(item.get("供应商英文名").toString());

                supplierbaseinforMapper.insert(supplierbaseinfor);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }*/
        return null;
    }

}
