package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.VO.StartprogramVo;
import com.nt.service_BASF.StartprogramServices;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.service_BASF.mapper.StartprogramMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    //获取未开班培训列表
    @Override
    public List<Startprogram> nostart() throws Exception {
        Startprogram startprogram = new Startprogram();
        startprogram.setStatus("0");
        startprogram.setProgramtype("0");
        return startprogramMapper.select(startprogram);
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
            startprogramVoList.add(startprogramVo);
        }
        return startprogramVoList;
    }

    //删除培训
    @Override
    public void delete(Startprogram startprogram, TokenModel tokenModel) throws Exception {
        startprogram.preUpdate(tokenModel);
        startprogramMapper.updateByPrimaryKey(startprogram);
    }

}
