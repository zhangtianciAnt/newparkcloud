package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.service_pfans.PFANS2000.BonussendService;
import com.nt.service_pfans.PFANS2000.LunarbonusService;
import com.nt.service_pfans.PFANS2000.mapper.BonussendMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunarbonusMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class LunarbonusServiceImpl implements LunarbonusService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LunarbonusMapper lunarbonusMapper;


    @Override
    public List<Lunarbonus> getList(Lunarbonus lunarbonus) throws Exception {
        return lunarbonusMapper.select(lunarbonus);
    }

    @Override
    public void insert(Lunarbonus lunarbonus, TokenModel tokenModel) throws Exception {
        lunarbonus.preInsert(tokenModel);
        lunarbonus.setLunarbonus_id(UUID.randomUUID().toString());
        lunarbonus.setEvaluationday(new Date());
        lunarbonusMapper.insert(lunarbonus);
    }
}
