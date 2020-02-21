package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_BASF.Highriskarea;
import com.nt.dao_BASF.Highriskareadetailed;
import com.nt.dao_BASF.Riskassessment;
import com.nt.dao_BASF.VO.HighriskareaVo;
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

    @Autowired
    private HighriskareadetailedMapper highriskareadetailedMapper;

    //excel成绩文档导入
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importexcel(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        //存放消息
        List<String> result = new ArrayList<>();
        //临时文件
        File f = null;
        //插入成功数
        int successCount = 0;
        //插入失败数
        int errorCount = 0;
        //excel集合
        List<List<Object>> list;
        //转换后的excel集合
        List<List<String>> strList = new ArrayList<>();
        //当前执行行数
        int k = 1;
        try {
            //转换成文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取并存储为临时文件时发生未知异常！");
            return result;
        }
        try {
            //读取excel临时文件
            ExcelReader reader = ExcelUtil.getReader(f);
            //读取到的excel集合
            list = reader.read();
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取Excel临时文件时发生未知异常！");
            return result;
        }
        try {
            //判断是否是空文件
            if (list == null) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("空文件，请上传正确的文件");
                return result;
            }
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("文件格式不正确！");
            return result;
        }
        try {
            try {
                //转换为sing的list集合
                for (List<Object> list1 : list) {
                    List<String> stringList = new ArrayList<>();
                    for (Object obj : list1) {
                        stringList.add(obj.toString());
                    }
                    strList.add(stringList);
                }
            } catch (Exception e) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("系统在数据获取时发生未知异常！");
                return result;
            }
            Query query = new Query();
            Riskassessment riskassessment = mongoTemplate.findOne(query, Riskassessment.class);
            try {
                //进行值的判断和赋值
                for (int i = 0; i < strList.size(); i++) {
                    for (int j = 0; j < strList.get(i).size(); j++) {
                        //设置值
                        if (StringUtils.isNotBlank(strList.get(i).get(j).trim())) {
                            successCount += setData(strList.get(i).get(j).trim(), strList, i, j, riskassessment);
                        }
                    }
                }
                riskassessment.preUpdate(tokenModel);
            } catch (Exception e) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("系统在数据处理时发生未知异常！");
                return result;
            }
            try {
                mongoTemplate.save(riskassessment);
            } catch (Exception e) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("系统在更新数据时发生未知异常！");
                return result;
            }

        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
        try {
            result.add("失败数：" + errorCount);
            result.add("成功数：" + successCount);
            return result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    public int setData(String str, List<List<String>> strList, int i, int j, Riskassessment riskassessment) throws Exception {
        switch (str) {
            case "生产设施（装置/生产线）":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setDevicenum(getData(strList, i, j));
                return 1;
            case "运行设施":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setRundevicenum(getData(strList, i, j));
                return 1;
            case "停产设施":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setStopdevicenum(getData(strList, i, j));
                return 1;
            case "储存设施（储罐）":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setVesselnum(getData(strList, i, j));
                return 1;
            case "在用储罐":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setEmployvesslnum(getData(strList, i, j));
                return 1;
            case "停用储罐":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setStopvesselnum(getData(strList, i, j));
                return 1;
            case "储存设施（仓库）":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setWarehousenum(getData(strList, i, j));
                return 1;
            case "在用仓库":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setEmploywarehousenum(getData(strList, i, j));
                return 1;
            case "停用仓库":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setStopwarehousenum(getData(strList, i, j));
                return 1;
            case "特殊动火":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setSpecialhotworknum(getData(strList, i, j));
                return 1;
            case "一级动火":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setOnelevelhotworknum(getData(strList, i, j));
                return 1;
            case "二级动火":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setTwolevelhotworknum(getData(strList, i, j));
                return 1;
            case "受限空间作业":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setConfinedspacenum(getData(strList, i, j));
                return 1;
            case "断路作业":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setRoadbreakingnum(getData(strList, i, j));
                return 1;
            case "动土作业":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setExcavationnum(getData(strList, i, j));
                return 1;
            case "盲板抽堵作业":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setBlindplateoperationnum(getData(strList, i, j));
                return 1;
            case "临时用电":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setTemporaryelectricitynum(getData(strList, i, j));
                return 1;
            case "高处作业":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setWorkatheightnum(getData(strList, i, j));
                return 1;
            case "吊装作业":
                if (getData(strList, i, j) != null)
                    riskassessment.getSynthesize().setLiftingoperation(getData(strList, i, j));
                return 1;

        }
        return 0;
    }

    public Integer getData(List<List<String>> strList, int i, int j) throws Exception {
        Integer integer = null;
        for (int a = 0; a < 3; a++) {
            if (strList.get(i).get(j + a).equals("套") || strList.get(i).get(j + a).equals("座") || strList.get(i).get(j + a).equals("次")) {
                break;
            } else {
                try {
                    integer = Integer.parseInt(strList.get(i).get(j + a));
                    return integer;
                } catch (NumberFormatException e) {

                }
            }
        }
        return integer;
    }

    //获取数据
    @Override
    public Riskassessment getData() throws Exception {
        Query query = new Query();
        return mongoTemplate.findOne(query, Riskassessment.class);
    }

    //承诺公告更新
    @Override
    public void noticeUpdata(String notice, TokenModel tokenModel) throws Exception {
        Query query = new Query();
        Riskassessment riskassessment = mongoTemplate.findOne(query, Riskassessment.class);
        riskassessment.setNotice(notice);
        riskassessment.preUpdate(tokenModel);
        mongoTemplate.save(riskassessment);
    }

    @Override
    public List<Highriskarea> selecthig(TokenModel tokenModel,Highriskarea highriskarea) throws Exception {
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

    @Override
    public void delete(TokenModel tokenModel,Highriskarea highriskarea) throws Exception {
        //逻辑删除（status -> "1"）
        highriskareaMapper.updateByPrimaryKeySelective(highriskarea);
    }
}
