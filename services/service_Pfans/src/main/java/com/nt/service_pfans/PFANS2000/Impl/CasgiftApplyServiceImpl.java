package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.service_pfans.PFANS2000.CasgiftApplyService;
import com.nt.service_pfans.PFANS2000.mapper.CasgiftApplyMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class CasgiftApplyServiceImpl implements CasgiftApplyService {

    @Autowired
    private CasgiftApplyMapper casgiftapplyMapper;

    @Override
    public List<CasgiftApply> getCasgiftApply(CasgiftApply casgiftapply ) {
        return casgiftapplyMapper.select(casgiftapply);
    }

    @Override
    public CasgiftApply One(String casgiftapplyid) throws Exception {

        return casgiftapplyMapper.selectByPrimaryKey(casgiftapplyid);
    }

    @Override
    public void updateCasgiftApply(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception {
        casgiftapplyMapper.updateByPrimaryKey(casgiftapply);
    }

    @Override
    public void insert(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception {

        casgiftapply.preInsert(tokenModel);
        casgiftapply.setCasgiftapplyid(UUID.randomUUID().toString());
        casgiftapplyMapper.insert(casgiftapply);
    }

    @Override
    public List<CasgiftApply> getCasgiftApplyList(CasgiftApply casgiftapply, HttpServletRequest request) throws Exception {

        return casgiftapplyMapper.select(casgiftapply) ;
    }

    @Override
    public void updateCasgiftApplyList(List<CasgiftApply> casgiftapply, TokenModel tokenModel) throws Exception {
        String strTenantid = casgiftapply.get(0).getTenantid();
        for (CasgiftApply ca : casgiftapply){
            ca.preUpdate(tokenModel);
            if(strTenantid.equals("0")){
                ca.setReleasedate(DateUtil.format(new Date(),"yyyy-MM"));
            }
            else{
                ca.setReleasedate("");
            }
            casgiftapplyMapper.updateByPrimaryKey(ca);
        }
    }
}
