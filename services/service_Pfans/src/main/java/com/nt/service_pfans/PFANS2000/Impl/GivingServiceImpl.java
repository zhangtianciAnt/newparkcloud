package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.GivingVo;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private OtherFiveMapper otherfiveMapper;

    @Autowired
    private AppreciationMapper appreciationMapper;


    /**
     * 生成基数表
     * FJL
     */
    @Override
    public GivingVo List(String giving_id) throws Exception {
        GivingVo GivingVo = new GivingVo();
        Giving Giving = new Giving();
        Giving.setGiving_id(giving_id);
        GivingVo.setGiving(Giving);

        OtherTwo othertwo = new OtherTwo();
        othertwo.setGiving_id(giving_id);
        List<OtherTwo> othertwolist = othertwoMapper.select(othertwo);
        othertwolist = othertwolist.stream().sorted(Comparator.comparing(OtherTwo::getRowindex)).collect(Collectors.toList());
        GivingVo.setOtherTwo(othertwolist);

        Appreciation appreciation = new Appreciation();
        appreciation.setGiving_id(giving_id);
        List<Appreciation> appreciationlist = appreciationMapper.select(appreciation);
        appreciationlist = appreciationlist.stream().sorted(Comparator.comparing(Appreciation::getRowindex)).collect(Collectors.toList());
        GivingVo.setAppreciation(appreciationlist);

        OtherFive otherfive = new OtherFive();
        otherfive.setGiving_id(giving_id);
        List<OtherFive> otherfivelist = otherfiveMapper.select(otherfive);
        otherfivelist = otherfivelist.stream().sorted(Comparator.comparing(OtherFive::getRowindex)).collect(Collectors.toList());
        GivingVo.setOtherFive(otherfivelist);

        Base base = new Base();
        base.setGiving_id(giving_id);
        List<Base> baselist = baseMapper.select(base);
        baselist = baselist.stream().sorted(Comparator.comparing(Base::getRowindex)).collect(Collectors.toList());
        GivingVo.setBase(baselist);
//
//        Contrast contrast = new Contrast();
//        contrast.setGiving_id(giving_id);
//        List<Contrast> contrastList = contrastMapper.select(contrast);
//        contrastList = contrastList.stream().sorted(Comparator.comparing(Contrast::getRowindex)).collect(Collectors.toList());
//        GivingVo.setContrast(contrastList);

        return GivingVo;
    }

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
//                base.setOwner(customer.getUserid());
                String departmentid = customer.getUserinfo().getDepartmentid().toString();
                String name = departmentid.replace("[", "").replace("]", "");
                base.setDepartment_id(name);  //部门[]
//              base.setRn(customer.get);  //RN
                base.setSex(customer.getUserinfo().getSex());  //性别
//                base.setOnlychild(customer.getUserinfo().getChildren());  //独生子女
                //入/退職/産休
//                base.setBonus(customer);  //奨金計上
                //1999年前社会人
                if (customer.getUserinfo().getRegister() == "大連") {
                    base.setRegistered("是"); //大連戸籍
                }
                base.setRegistered("-"); //大連戸籍

                //2019年6月
                //2019年7月
                base.setPension(customer.getUserinfo().getOldageinsurance()); //養老・失業・工傷基数
                base.setMedical(customer.getUserinfo().getMedicalinsurance()); //医療・生育基数

                base.setAccumulation(customer.getUserinfo().getHousefund());  //公积金基数
                //采暖费
                base.setWorkdate(customer.getUserinfo().getEnterday());     //入社日

                base.setRowindex(rowindex);
                baseMapper.insertSelective(base);
            }
        }
    }


    @Override
    public void insertOtherTwo(String givingid, TokenModel tokenModel) throws Exception {
        OtherTwo othertwo = new OtherTwo();
        CasgiftApply casgiftapply = new CasgiftApply();
        casgiftapply.setPayment("0");
        casgiftapply.setStatus("4");
        List<CasgiftApply> casgiftapplylist = casgiftapplyMapper.select(casgiftapply);
        othertwo.setType("0");
        othertwoMapper.delete(othertwo);
        int rowundex = 0;
        for (CasgiftApply casgift : casgiftapplylist) {
            rowundex = rowundex + 1;
            String othertwoid = UUID.randomUUID().toString();
            othertwo.preInsert(tokenModel);
            othertwo.setOthertwo_id(othertwoid);
            othertwo.setGiving_id(givingid);
            othertwo.setUser_id(casgift.getUser_id());
            Query query = new Query();
            String User_id = casgift.getUser_id();
            query.addCriteria(Criteria.where("userid").is(User_id));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            othertwo.setJobnumber(customerInfo.getUserinfo().getJobnumber());
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
    public void insertContrast(String givingid, TokenModel tokenModel) throws Exception {
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
//
//    @Override
//    public List<Base> getListtBase(Base base) throws Exception {
//        return baseMapper.select(base);
//    }

    @Override
    public void insert(String generation, TokenModel tokenModel) throws Exception {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
        String givingid = UUID.randomUUID().toString();
        Giving giving = new Giving();
        giving.preInsert(tokenModel);
        giving.setGiving_id(givingid);
        giving.setGeneration(generation);
        giving.setGenerationdate(new Date());
        giving.setMonths(sf1.format(new Date()));

        Giving giving1 = new Giving();
        String strTemp = sf1.format(new Date());
        giving1.setMonths(strTemp);
        givingMapper.delete(giving1);
        givingMapper.insert(giving);
        insertBase(givingid, tokenModel);
        insertContrast(givingid, tokenModel);
        insertOtherTwo(givingid, tokenModel);
    }

    @Override
    public List<Giving> getDataList(Giving giving) throws Exception {

        return givingMapper.select(giving);
    }
}
