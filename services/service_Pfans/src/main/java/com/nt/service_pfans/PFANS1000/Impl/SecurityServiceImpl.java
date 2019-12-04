package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Security;
import com.nt.dao_Pfans.PFANS1000.Securitydetail;
import com.nt.dao_Pfans.PFANS1000.Vo.SecurityVo;
import com.nt.service_pfans.PFANS1000.SecurityService;
import com.nt.service_pfans.PFANS1000.mapper.SecurityMapper;
import com.nt.service_pfans.PFANS1000.mapper.SecuritydetailMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private SecurityMapper securityMapper;

    @Autowired
    private SecuritydetailMapper securitydetailMapper;

    @Override
    public List<Security> getSecurity(Security security)  throws Exception{
        return securityMapper.select(security);
    }

    @Override
    public SecurityVo selectById(String securityid) throws Exception {
        SecurityVo asseVo = new SecurityVo();
        Securitydetail securitydetail = new Securitydetail();
        securitydetail.setSecurityid(securityid);
        List<Securitydetail> securitydetaillist = securitydetailMapper.select(securitydetail);
        securitydetaillist = securitydetaillist.stream().sorted(Comparator.comparing(Securitydetail::getRowindex)).collect(Collectors.toList());
        Security secur = securityMapper.selectByPrimaryKey(securityid);
        asseVo.setSecurity(secur);
        asseVo.setSecuritydetail(securitydetaillist);
        return asseVo;
    }

    @Override
    public void updateSecurity(SecurityVo securityVo, TokenModel tokenModel) throws Exception {
        Security security = new Security();
        BeanUtils.copyProperties(securityVo.getSecurity(), security);
        security.preUpdate(tokenModel);
        securityMapper.updateByPrimaryKey(security);
        String ssecurityid = security.getSecurityid();
        Securitydetail tail = new Securitydetail();
        tail.setSecurityid(ssecurityid);
        securitydetailMapper.delete(tail);
        List<Securitydetail> securitydetaillist = securityVo.getSecuritydetail();
        if (securitydetaillist != null) {
            int rowindex = 0;
            for (Securitydetail securitydetail : securitydetaillist) {
                rowindex = rowindex + 1;
                securitydetail.preInsert(tokenModel);
                securitydetail.setSecuritydetailid(UUID.randomUUID().toString());
                securitydetail.setSecurityid(ssecurityid);
                securitydetail.setRowindex(rowindex);
                securitydetailMapper.insertSelective(securitydetail);
            }
        }
    }
    @Override
    public void insert(SecurityVo securityVo, TokenModel tokenModel) throws Exception {
        String securityid = UUID.randomUUID().toString();
        Security security = new Security();
        BeanUtils.copyProperties(securityVo.getSecurity(), security);
        security.preInsert(tokenModel);
        security.setSecurityid(securityid);
        securityMapper.insertSelective(security);
        List<Securitydetail> securitydetaillist = securityVo.getSecuritydetail();
        if (securitydetaillist != null) {
            int rowindex = 0;
            for (Securitydetail securitydetail : securitydetaillist) {
                rowindex = rowindex + 1;
                securitydetail.preInsert(tokenModel);
                securitydetail.setSecuritydetailid(UUID.randomUUID().toString());
                securitydetail.setSecurityid(securityid);
                securitydetail.setRowindex(rowindex);
                securitydetailMapper.insertSelective(securitydetail);
            }
        }
    }
}
