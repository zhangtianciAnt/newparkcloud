package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Programlist;
import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.VO.PassingRateVo;
import com.nt.dao_BASF.VO.StartprogramVo;
import com.nt.dao_BASF.VO.TrainEducationPerVo;
import com.nt.dao_BASF.VO.TrainEducationPerVo2;
import com.nt.dao_Org.CustomerInfo;
import com.nt.service_BASF.ProgramlistServices;
import com.nt.service_BASF.StartprogramServices;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.service_BASF.mapper.ProgramlistMapper;
import com.nt.service_BASF.mapper.StartprogramMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: StartprogramServicesImpl
 * @Author: 王哲
 * @Description: 申请考核接口实现类
 * @Date: 2020/1/7 11:07
 * @Version: 1.0
 */
@Service
public class StartprogramServicesImpl implements StartprogramServices {

    @Autowired
    private StartprogramMapper startprogramMapper;

    @Autowired
    private TrainjoinlistServices trainjoinlistServices;

    @Autowired
    private ProgramlistServices programlistroServices;

    @Autowired
    private ProgramlistMapper programlistMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    //获取未开班培训列表
    @Override
    public List<Startprogram> nostart() throws Exception {
        Startprogram startprogram = new Startprogram();
        startprogram.setStatus("0");
        startprogram.setProgramtype("BC039001");
        startprogram.setIsonline("BC032002");
        return startprogramMapper.select(startprogram);
    }

    @Override
    public List<Startprogram> onlineNostart() throws Exception {
        Startprogram startprogram = new Startprogram();
        //未删除的
        startprogram.setStatus("0");
        //进行中的
        startprogram.setProgramtype("BC039002");
        //线上的
        startprogram.setIsonline("BC032001");
        return startprogramMapper.select(startprogram);
    }

    @Override
    public Startprogram one(String startprogramid) throws Exception {
        return startprogramMapper.selectByPrimaryKey(startprogramid);
    }

    //添加培训列表
    @Override
    public void insert(Startprogram startprogram, TokenModel tokenModel) throws Exception {
        startprogram.preInsert(tokenModel);
        startprogram.setStartprogramid(UUID.randomUUID().toString());
        startprogramMapper.insert(startprogram);
    }

    //更新培训列表
    @Override
    public void update(Startprogram startprogram, TokenModel tokenModel) throws Exception {
        startprogram.preUpdate(tokenModel);
        startprogramMapper.updateByPrimaryKeySelective(startprogram);
    }

    //更新培训清单
    @Override
    public void updateprogramlist(String startprogramid, TokenModel tokenModel) throws Exception {
        Startprogram startprogram = one(startprogramid);
        Programlist programlist = programlistroServices.one(startprogram.getProgramlistid());
        programlist.setLastdate(startprogram.getActualstartdate());
        programlist.setThisdate(null);
        programlist.setNumber(String.valueOf(Integer.parseInt(programlist.getNumber()) + 1));
        programlist.setNumberpeople(String.valueOf(Integer.parseInt(programlist.getNumberpeople()) + trainjoinlistServices.actualjoinnumber(startprogramid)));
        programlist.setThispeople("0");
        programlist.setProgramtype("BC039001");
        programlist.preUpdate(tokenModel);
        programlistMapper.updateByPrimaryKey(programlist);
    }

    //查询培训
    @Override
    public List<Startprogram> select(Startprogram startprogram) throws Exception {
        return startprogramMapper.select(startprogram);
    }

    //查询培训增强
    @Override
    public List<StartprogramVo> selectEnhance(Startprogram startprogram) throws Exception {
        List<StartprogramVo> startprogramVoList = new ArrayList<StartprogramVo>();
        for (Startprogram startprogram1 : startprogramMapper.select(startprogram)) {
            StartprogramVo startprogramVo = new StartprogramVo();
            startprogramVo.setStartprogram(startprogram1);
            startprogramVo.setJoinnumber(trainjoinlistServices.joinnumber(startprogram1.getStartprogramid()));
            startprogramVo.setActualjoinnumber(trainjoinlistServices.actualjoinnumber(startprogram1.getStartprogramid()));
            startprogramVo.setThroughjoinnumber(trainjoinlistServices.throughjoinnumber(startprogram1.getStartprogramid()));
            startprogramVo.setThroughAndNoThrough(trainjoinlistServices.throughAndNoThrough(startprogram1.getStartprogramid()));
            startprogramVoList.add(startprogramVo);
        }
        return startprogramVoList;
    }

    //删除培训
    @Override
    public void delete(Startprogram startprogram, TokenModel tokenModel) throws Exception {
        startprogram.preUpdate(tokenModel);
        startprogramMapper.updateByPrimaryKeySelective(startprogram);
    }

    //by人员id查询培训项目
    @Override
    public List<Startprogram> selectbyuserid(String userid, String selecttype) throws Exception {
        return startprogramMapper.selectbyuserid(userid, selecttype);
    }

    @Override
    public List<PassingRateVo> getMandatoryInfo() throws Exception {
        return startprogramMapper.getMandatoryInfo();
    }

    @Override
    public List<PassingRateVo> getIsMandatoryInfo() throws Exception {
        return startprogramMapper.getIsMandatoryInfo();
    }

    //根据姓名（或员工号、卡号）和年份查询某人员培训信息（培训教育大屏用）
    @Override
    public TrainEducationPerVo getTrainEducationPerInfo(String year, String parameter) throws Exception {

        TrainEducationPerVo tepv = new TrainEducationPerVo();
        //用户数据
        List<CustomerInfo> customerInfoName;
        List<CustomerInfo> customerInfoJobnumber = new ArrayList<>();
        List<CustomerInfo> customerInfoDocumentnumber = new ArrayList<>();
        //根据传来的参数判断查找唯一数据
        //按姓名
        Query queryName = new Query();
        queryName.addCriteria(Criteria.where("userinfo.customername").is(parameter));
        customerInfoName = mongoTemplate.find(queryName, CustomerInfo.class);
        if (customerInfoName.size() != 1) {
            //如不唯一  按员工号
            Query queryJobnumber = new Query();
            queryJobnumber.addCriteria(Criteria.where("userinfo.jobnumber").is(parameter));
            customerInfoJobnumber = mongoTemplate.find(queryJobnumber, CustomerInfo.class);
            if (customerInfoJobnumber.size() != 1) {
                //如不唯一 按卡号（use id）
                Query queryDocumentnumber = new Query();
                queryDocumentnumber.addCriteria(Criteria.where("userinfo.documentnumber").is(parameter));
                customerInfoDocumentnumber = mongoTemplate.find(queryDocumentnumber, CustomerInfo.class);
            }
        }
        //如果存在唯一数据
        if (customerInfoName.size() == 1 || customerInfoJobnumber.size() == 1 || customerInfoDocumentnumber.size() == 1) {
            tepv.setState(true);
            if (customerInfoName.size() == 1) {
                tepv.setPersonnelid(customerInfoName.get(0).get_id());
                tepv.setCustomername(customerInfoName.get(0).getUserinfo().getCustomername());
                tepv.setJobnumber(customerInfoName.get(0).getUserinfo().getJobnumber());
                tepv.setDocumentnumber(customerInfoName.get(0).getUserinfo().getDocumentnumber());
            } else if (customerInfoJobnumber.size() == 1) {
                tepv.setPersonnelid(customerInfoJobnumber.get(0).get_id());
                tepv.setCustomername(customerInfoJobnumber.get(0).getUserinfo().getCustomername());
                tepv.setJobnumber(customerInfoJobnumber.get(0).getUserinfo().getJobnumber());
                tepv.setDocumentnumber(customerInfoJobnumber.get(0).getUserinfo().getDocumentnumber());
            } else if (customerInfoDocumentnumber.size() == 1) {
                tepv.setPersonnelid(customerInfoDocumentnumber.get(0).get_id());
                tepv.setCustomername(customerInfoDocumentnumber.get(0).getUserinfo().getCustomername());
                tepv.setJobnumber(customerInfoDocumentnumber.get(0).getUserinfo().getJobnumber());
                tepv.setDocumentnumber(customerInfoDocumentnumber.get(0).getUserinfo().getDocumentnumber());
            }
            tepv.setThelengthSum(startprogramMapper.getTrainThelength(year, tepv.getPersonnelid()));
            tepv.setStartprograms(startprogramMapper.getTrainEducationPerInfo(year, tepv.getPersonnelid()));
            tepv.setDepartmentname(startprogramMapper.getDepartmentname(year, tepv.getPersonnelid()));
            return tepv;

        }
        //不存在唯一数据
        else {
            tepv.setState(false);
            return tepv;
        }
    }

    //查找对应的培训信息（培训教育大屏用）
    private List<TrainEducationPerVo2> startprograms(String year, String personnelid) throws Exception {
        return startprogramMapper.getTrainEducationPerInfo(year, personnelid);
    }

    //大屏培训信息推送列表
    @Override
    public List<Startprogram> getFutureProgram() throws Exception {
        return startprogramMapper.getFutureProgram();
    }


}
