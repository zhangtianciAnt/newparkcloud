package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_BASF.Highriskarea;
import com.nt.dao_BASF.Riskassessment;
import com.nt.service_BASF.RiskassessmentServices;
import com.nt.service_BASF.mapper.HighriskareaMapper;
import com.nt.service_BASF.mapper.HighriskareadetailedMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: RiskassessmentServicesImpl
 * @Author: 王哲
 * @Description: 风险判研表接口实现类
 * @Date: 2020/02/04 10:37
 * @Version: 1.0
 */
@Service
public class RiskassessmentServicesImpl implements RiskassessmentServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HighriskareaMapper highriskareaMapper;


    //获取风险研判数据（MongoDB）
    @Override
    public Riskassessment getData() throws Exception {
        Query query = new Query();
        return mongoTemplate.findOne(query, Riskassessment.class);
    }

    //风险研判承诺公告更新
    @Override
    public void noticeUpdata(String notice, TokenModel tokenModel) throws Exception {
        Query query = new Query();
        Riskassessment riskassessment = mongoTemplate.findOne(query, Riskassessment.class);
        riskassessment.setNotice(notice);
        riskassessment.preUpdate(tokenModel);
        mongoTemplate.save(riskassessment);
    }

    //region 高风险作业
    //高风险作业查找
    @Override
    public List<Highriskarea> selecthig(TokenModel tokenModel, Highriskarea highriskarea) throws Exception {
        return highriskareaMapper.select(highriskarea);

//        List<HighriskareaVo> highriskareavolist = new ArrayList<>();//
//        List<Highriskarea> highriskareaList =  highriskareaMapper.select(highriskarea);//
//        for(int i = 0;i<highriskareaList.size();i++)
//        {
//            String id = highriskareaList.get(i).getHighriskareaid();
//            String name = highriskareaList.get(i).getHighriskareaname();
//            String detailedlocation = highriskareaList.get(i).getDetailedlocation();
//            String mapid = highriskareaList.get(i).getMapid();
//            String content = highriskareaList.get(i).getHighriskareacontent();
//            Highriskareadetailed highriskareadetailed = new Highriskareadetailed();
//            highriskareadetailed.setHighriskareaid(id);
//            List<Highriskareadetailed> highriskareadetailedlist = highriskareadetailedMapper.select(highriskareadetailed);
//            HighriskareaVo  highriskareavo  = new HighriskareaVo();
//            highriskareavo.setHighriskareaid(id);
//            highriskareavo.setHighriskareaname(name);
//            highriskareavo.setDetailedlocation(detailedlocation);
//            highriskareavo.setMapid(mapid);
//            highriskareavo.setHighriskareacontent(content);
//            ArrayList list = new ArrayList();
//            for(int j = 0;j<highriskareadetailedlist.size();j++)
//            {
//                String coordinate = highriskareadetailedlist.get(j).getCoordinate();
//                list.add(coordinate);
//            }
//            highriskareavo.setHighriskareadetailedlist(list);
//            highriskareavolist.add(highriskareavo);
//        }
//        return highriskareavolist;
    }

    //高风险作业添加
    @Override
    public void insert(TokenModel tokenModel, Highriskarea highriskarea) throws Exception {
        //Highriskarea highriskarea = new Highriskarea();
        highriskarea.preInsert(tokenModel);
        String id = UUID.randomUUID().toString();
        highriskarea.setHighriskareaid(id);
//        highriskarea.setHighriskareaname(highriskarea.getHighriskareaname());
//        highriskarea.setDetailedlocation(highriskarea.getDetailedlocation());
        highriskareaMapper.insert(highriskarea);

//        if (highriskareaVo.getHighriskareadetailedlist().size() >0) {
//            for (int i = 0; i < highriskareaVo.getHighriskareadetailedlist().size(); i++) {
//                String coordinate = highriskareaVo.getHighriskareadetailedlist().get(i).toString();
//                Highriskareadetailed highriskareadetailed = new Highriskareadetailed();
//                highriskareadetailed.preInsert(tokenModel);
//                highriskareadetailed.setHighriskareadetailedid(UUID.randomUUID().toString());
//                highriskareadetailed.setHighriskareaid(id);
//                highriskareadetailed.setCoordinate(coordinate);
//                highriskareadetailedMapper.insert(highriskareadetailed);
//            }
//        }
    }

    //高风险作业更新
    @Override
    public void update(TokenModel tokenModel, Highriskarea highriskarea) throws Exception {
        //Highriskarea highriskarea = new Highriskarea();
        highriskarea.preUpdate(tokenModel);
//        highriskarea.setHighriskareaid(highriskareaVo.getHighriskareaid());
//        highriskarea.setHighriskareaname(highriskareaVo.getHighriskareaname());
//        highriskarea.setDetailedlocation(highriskareaVo.getDetailedlocation());
        highriskareaMapper.updateByPrimaryKeySelective(highriskarea);
//        Highriskareadetailed highriskareadetailed = new Highriskareadetailed();
//        highriskareadetailed.setHighriskareaid(highriskareaVo.getHighriskareaid());
//        highriskareadetailedMapper.delete(highriskareadetailed);
//
//        if (highriskareaVo.getHighriskareadetailedlist().size() >0) {
//            for (int i = 0; i < highriskareaVo.getHighriskareadetailedlist().size(); i++) {
//                String coordinate = highriskareaVo.getHighriskareadetailedlist().get(i).toString();
//                Highriskareadetailed highriskareadetailednew = new Highriskareadetailed();
//                highriskareadetailednew.preInsert(tokenModel);
//                highriskareadetailednew.setHighriskareadetailedid(UUID.randomUUID().toString());
//                highriskareadetailednew.setHighriskareaid(highriskareaVo.getHighriskareaid());
//                highriskareadetailednew.setCoordinate(coordinate);
//                highriskareadetailedMapper.insert(highriskareadetailednew);
//            }
//        }
    }

    //高风险作业删除
    @Override
    public void delete(TokenModel tokenModel, Highriskarea highriskarea) throws Exception {
        //逻辑删除（status -> "1"）
        highriskareaMapper.updateByPrimaryKeySelective(highriskarea);
    }
    //endregion
}
