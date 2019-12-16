package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Psdcd;
import com.nt.dao_Pfans.PFANS1000.Psdcddetail;
import com.nt.dao_Pfans.PFANS1000.Vo.PsdcdVo;
import com.nt.service_pfans.PFANS1000.PsdcdService;
import com.nt.service_pfans.PFANS1000.mapper.PsdcdMapper;
import com.nt.service_pfans.PFANS1000.mapper.PsdcddetailMapper;
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
@Transactional(rollbackFor = Exception.class)
public class PsdcdServiceImpl  implements PsdcdService {

    @Autowired
    private PsdcdMapper psdcdMapper;

    @Autowired
    private PsdcddetailMapper psdcddetailMapper;

    @Override
    public List<Psdcd> getPsdcd(Psdcd psdcd)  throws Exception{
        return psdcdMapper.select(psdcd);
    }

    @Override
    public PsdcdVo selectById(String psdcd_id) throws Exception {
        PsdcdVo asseVo = new PsdcdVo();
        Psdcddetail psdcddetail = new Psdcddetail();
        psdcddetail.setPsdcd_id(psdcd_id);
        List<Psdcddetail> psdcddetaillist = psdcddetailMapper.select(psdcddetail);
        psdcddetaillist = psdcddetaillist.stream().sorted(Comparator.comparing(Psdcddetail::getRowindex)).collect(Collectors.toList());
        Psdcd secur = psdcdMapper.selectByPrimaryKey(psdcd_id);
        asseVo.setPsdcd(secur);
        asseVo.setPsdcddetail(psdcddetaillist);
        return asseVo;
    }

    @Override
    public void update(PsdcdVo psdcdVo, TokenModel tokenModel) throws Exception {
        Psdcd psdcd = new Psdcd();
        BeanUtils.copyProperties(psdcdVo.getPsdcd(), psdcd);
        psdcd.preUpdate(tokenModel);
        psdcdMapper.updateByPrimaryKey(psdcd);
        String psdcd_id = psdcd.getPsdcd_id();
        Psdcddetail tail = new Psdcddetail();
        tail.setPsdcd_id(psdcd_id);
        psdcddetailMapper.delete(tail);
        List<Psdcddetail> psdcddetaillist = psdcdVo.getPsdcddetail();
        if (psdcddetaillist != null) {
            int rowindex = 0;
            for (Psdcddetail psdcddetail : psdcddetaillist) {
                rowindex = rowindex + 1;
                psdcddetail.preInsert(tokenModel);
                psdcddetail.setPsdcddetail_id(UUID.randomUUID().toString());
                psdcddetail.setPsdcd_id(psdcd_id);
                psdcddetail.setRowindex(rowindex);
                psdcddetailMapper.insertSelective(psdcddetail);
            }
        }
    }
    @Override
    public void insert(PsdcdVo psdcdVo, TokenModel tokenModel) throws Exception {
        String psdcd_id = UUID.randomUUID().toString();
        Psdcd psdcd = new Psdcd();
        BeanUtils.copyProperties(psdcdVo.getPsdcd(), psdcd);
        psdcd.preInsert(tokenModel);
        psdcd.setPsdcd_id(psdcd_id);
        psdcdMapper.insertSelective(psdcd);
        List<Psdcddetail> psdcddetaillist = psdcdVo.getPsdcddetail();
        if (psdcddetaillist != null) {
            int rowindex = 0;
            for (Psdcddetail psdcddetail : psdcddetaillist) {
                rowindex = rowindex + 1;
                psdcddetail.preInsert(tokenModel);
                psdcddetail.setPsdcddetail_id(UUID.randomUUID().toString());
                psdcddetail.setPsdcd_id(psdcd_id);
                psdcddetail.setRowindex(rowindex);
                psdcddetail.setUsertype(psdcddetail.getUsertype());
                psdcddetail.setUsername(psdcddetail.getUsername());
                psdcddetail.setSurname(psdcddetail.getSurname());
                psdcddetail.setMing(psdcddetail.getMing());
                psdcddetail.setAccount(psdcddetail.getAccount());
                psdcddetail.setTransmission(psdcddetail.getTransmission());
                psdcddetail.setWaitfortime(psdcddetail.getWaitfortime());
                psdcddetail.setBudgetunit(psdcddetail.getBudgetunit());
                psdcddetail.setCybozu(psdcddetail.getCybozu());
                psdcddetail.setExpecttime(psdcddetail.getExpecttime());
                psdcddetail.setDomainaccount(psdcddetail.getDomainaccount());
                psdcddetail.setForwardtime(psdcddetail.getForwardtime());
                psdcddetail.setPreparefor(psdcddetail.getPreparefor());
                psdcddetail.setCybozu(psdcddetail.getCybozu());
                psdcddetail.setExpecttime(psdcddetail.getExpecttime());
                psdcddetailMapper.insertSelective(psdcddetail);
            }
        }
    }

}
