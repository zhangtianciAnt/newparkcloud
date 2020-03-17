package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Examinationproject;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.service_pfans.PFANS2000.LunardetailService;
import com.nt.service_pfans.PFANS2000.mapper.ExaminationprojectMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunarbonusMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunardetailMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class LunardetailServiceImpl implements LunardetailService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LunarbonusMapper lunarbonusMapper;

    @Autowired
    private LunardetailMapper lunardetailMapper;

    @Autowired
    private ExaminationprojectMapper examinationprojectMapper;


    //获取详细一览
    @Override
    public List<Lunardetail> getLunardetail(LunardetailVo lunardetailVo) throws Exception {
        Lunardetail lunardetail = new Lunardetail();
        lunardetail.setLunarbonus_id(lunardetailVo.getLunarbonus_id());
        lunardetail.setEvaluatenum(lunardetailVo.getEvaluatenum());
        lunardetail.setSubjectmon(lunardetailVo.getSubjectmon());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String evaluationday = simpleDateFormat.format(lunardetailVo.getEvaluationday());
        lunardetail.setEvaluationday(evaluationday);
        lunardetail.setExaminationobject_id(lunardetailVo.getExaminationobject_id());

//
//        Examinationproject examinationproject = new Examinationproject();
//        examinationproject.setExaminationobject_id(lunardetailVo.getExaminationobject_id());
//        List<Examinationproject> list =  examinationprojectMapper.select(examinationproject);

        return lunardetailMapper.select(lunardetail);
    }
//获取详情状态
    @Override
    public List<Examinationproject> getExam(String id) throws Exception {
        Examinationproject e = new Examinationproject();
        e.setExaminationobject_id(id);
        List<Examinationproject>  ExaminationprojectList =  examinationprojectMapper.select(e);
        ExaminationprojectList = ExaminationprojectList.stream().sorted(Comparator.comparing(Examinationproject::getIndex)).collect(Collectors.toList());
        return ExaminationprojectList;
    }

    @Override
    public void update(List<Lunardetail> lunardetailList, TokenModel tokenModel) throws Exception {
//        lunardetailMapper.updateByPrimaryKey(lunardetail);
        for(int i = 0; i < lunardetailList.size(); i++){
            Lunardetail lunardetail = lunardetailList.get(i);
            lunardetailMapper.updateByPrimaryKey(lunardetail);
        }
    }
}
