package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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

    @Autowired
    private ContrastMapper contrastMapper;


    @Autowired
    private CasgiftApplyMapper casgiftapplyMapper;

    @Autowired
    private OtherTwoMapper othertwoMapper;

    /**
     * 生成基数表
     * FJL
     */
    @Override
    public void insertBase(String givingid, TokenModel tokenModel) throws Exception {
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
    public void insertOtherTwo(String givingid, TokenModel tokenModel) throws Exception {
        CasgiftApply casgiftapply = new CasgiftApply();
        casgiftapply.setPayment("0");
        casgiftapply.setStatus("4");
        List<CasgiftApply> casgiftapplylist = casgiftapplyMapper.select(casgiftapply);
        int rowundex = 0;
        for (CasgiftApply casgift : casgiftapplylist) {
            rowundex = rowundex + 1;
            OtherTwo othertwo = new OtherTwo();
            String othertwoid = UUID.randomUUID().toString();
            othertwo.preInsert(tokenModel);
            othertwo.setOthertwo_id(othertwoid);
            othertwo.setGiving_id(givingid);
            othertwo.setType("0");
            othertwo.setRowindex(rowundex);
            othertwo.setRootknot(casgift.getTwoclass());
            othertwo.setMoneys(casgift.getAmoutmoney());
            othertwoMapper.insertSelective(othertwo);
        }
    }

    /**
     * 生成基数表
     * FJL
     */
    @Override
    public void insertBase1(String givingid, TokenModel tokenModel) throws Exception {
        Base base = new Base();
        base.setGiving_id(givingid);
        List<Base> baselist = baseMapper.select(base);
        if (baselist != null) {
            for (Base base1 : baselist) {
                Contrast contrast = new Contrast();
                String consrastid = UUID.randomUUID().toString();
                contrast.preInsert(tokenModel);
                contrast.setGiving_id(base1.getGiving_id());
                contrast.setContrast_id(consrastid);
                contrast.setUser_id(base1.getUser_id());
                contrast.setOwner(base1.getUser_id());
                contrast.setDepartment_id(base1.getDepartment_id());

                contrastMapper.insertSelective(contrast);
            }
        }
    }

    @Override
    public List<Base> getListtBase(Base base) throws Exception {
        return baseMapper.select(base);
    }

    @Override
    public void insert(String generation, TokenModel tokenModel) throws Exception {
//        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM");
        Giving giving1 = new Giving();
//        String strTemp = sf1.format(new Date());
//        Date delDate = sf1.parse(strTemp);
        giving1.setGenerationdate(new Date());
        givingMapper.delete(giving1);

        String givingid = UUID.randomUUID().toString();
        Giving giving = new Giving();
        giving.preInsert(tokenModel);
        giving.setGiving_id(givingid);
        giving.setGeneration(generation);
//        String strTemp1 = sf1.format(new Date());
//        Date delDate1 = sf1.parse(strTemp1);
        giving.setGenerationdate(new Date());
        ;
        givingMapper.insert(giving);
        insertBase(givingid, tokenModel);
        insertBase1(givingid, tokenModel);
    }

    @Override
    public List<Giving> getDataList(Giving giving) throws Exception {

        return givingMapper.select(giving);
    }
}
