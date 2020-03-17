package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Examinationobject;
import com.nt.service_pfans.PFANS2000.ExaminationobjectService;
import com.nt.service_pfans.PFANS2000.mapper.ExaminationobjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class ExaminationobjectServiceImpl implements ExaminationobjectService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ExaminationobjectMapper examinationobjectMapper;

    //获取一览
    @Override
    public List<Examinationobject> getList() throws Exception {
        return examinationobjectMapper.select(new Examinationobject());
        }

    }




