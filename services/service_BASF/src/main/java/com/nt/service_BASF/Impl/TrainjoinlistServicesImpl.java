package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Trainjoinlist;
import com.nt.dao_BASF.VO.TrainjoinlistVo;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.service_BASF.mapper.TrainjoinlistMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: TrainjoinlistServicesImpl
 * @Author: Newtouch
 * @Description: 培训参加名单接口实现类
 * @Date: 2020/1/7 11:14
 * @Version: 1.0
 */
@Service
public class TrainjoinlistServicesImpl implements TrainjoinlistServices {

    @Autowired
    private TrainjoinlistMapper trainjoinlistMapper;

    //添加培训人员名单
    @Override
    public void insert(TrainjoinlistVo trainjoinlistVo, TokenModel tokenModel) throws Exception {
        if (StringUtils.isNotBlank(trainjoinlistVo.getStartprogramid())) {
            //添加之前先删除之前的
            delete(trainjoinlistVo.getStartprogramid(), tokenModel);
            //循环添加
            for (String personnelid : trainjoinlistVo.getPersonnelid()) {
                Trainjoinlist trainjoinlist = new Trainjoinlist();
                if (StringUtils.isNotBlank(personnelid)) {
                    trainjoinlist.setPersonnelid(personnelid);
                    trainjoinlist.setStartprogramid(trainjoinlistVo.getStartprogramid());
                    trainjoinlist.preInsert(tokenModel);
                    trainjoinlist.setTrainjoinlistid(UUID.randomUUID().toString());
                    trainjoinlistMapper.insert(trainjoinlist);
                }
            }
        }
    }
    //根据培训列表删除参加名单
    @Override
    public void delete(String startprogramid, TokenModel tokenModel) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        for (Trainjoinlist trainjoinlist1 : trainjoinlistMapper.select(trainjoinlist)) {
            trainjoinlistMapper.delete(trainjoinlist1);
        }
    }


    //获取培训申请人员名单
    @Override
    public ArrayList<String> joinlist(String startprogramid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setStatus("0");
        List<Trainjoinlist> trainjoinlists = trainjoinlistMapper.select(trainjoinlist);
        ArrayList<String> joinlist = new ArrayList<String>();
        for (Trainjoinlist trainjoin : trainjoinlists) {
            if (StringUtils.isNotBlank(trainjoin.getPersonnelid())) {
                joinlist.add(trainjoin.getPersonnelid());
            }
        }
        return joinlist;
    }

    //根据人员id获取培训列表id
    @Override
    public List<Trainjoinlist> startprogramidList(String personnelid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setPersonnelid(personnelid);
        trainjoinlist.setStatus("0");
        return trainjoinlistMapper.select(trainjoinlist);
    }

    //根据培训列表主键获取申请人员总数
    @Override
    public int joinnumber(String startprogramid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setStatus("0");
        return trainjoinlistMapper.selectCount(trainjoinlist);
    }

    //根据培训列表主键获取实际参加人数
    public int actualjoinnumber(String startprogramid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setJointype("1");
        trainjoinlist.setStatus("0");
        return trainjoinlistMapper.selectCount(trainjoinlist);
    }

}
