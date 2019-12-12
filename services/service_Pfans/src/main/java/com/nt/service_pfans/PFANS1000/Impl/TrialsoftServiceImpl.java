package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Trialsoft;
import com.nt.dao_Pfans.PFANS1000.Trialsoftdetail;
import com.nt.dao_Pfans.PFANS1000.Vo.TrialsoftVo;
import com.nt.service_pfans.PFANS1000.TrialsoftService;
import com.nt.service_pfans.PFANS1000.mapper.TrialsoftMapper;
import com.nt.service_pfans.PFANS1000.mapper.TrialsoftdetailMapper;
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
public class TrialsoftServiceImpl implements TrialsoftService {

    @Autowired
    private TrialsoftMapper trialsoftMapper;

    @Autowired
    private TrialsoftdetailMapper trialsoftdetailMapper;

    @Override
    public List<Trialsoft> getTrialsoft(Trialsoft trialsoft)  throws Exception{
        return trialsoftMapper.select(trialsoft);
    }

    @Override
    public TrialsoftVo selectById(String trialsoft_id) throws Exception {
        TrialsoftVo asseVo = new TrialsoftVo();
        Trialsoftdetail trialsoftdetail = new Trialsoftdetail();
        trialsoftdetail.setTrialsoft_id(trialsoft_id);
        List<Trialsoftdetail> trialsoftdetaillist = trialsoftdetailMapper.select(trialsoftdetail);
        trialsoftdetaillist = trialsoftdetaillist.stream().sorted(Comparator.comparing(Trialsoftdetail::getRowindex)).collect(Collectors.toList());
        Trialsoft secur = trialsoftMapper.selectByPrimaryKey(trialsoft_id);
        asseVo.setTrialsoft(secur);
        asseVo.setTrialsoftdetail(trialsoftdetaillist);
        return asseVo;
    }

    @Override
    public void update(TrialsoftVo trialsoftVo, TokenModel tokenModel) throws Exception {
        Trialsoft trialsoft = new Trialsoft();
        BeanUtils.copyProperties(trialsoftVo.getTrialsoft(), trialsoft);
        trialsoft.preUpdate(tokenModel);
        trialsoftMapper.updateByPrimaryKey(trialsoft);
        String trialsoft_id = trialsoft.getTrialsoft_id();
        Trialsoftdetail tail = new Trialsoftdetail();
        tail.setTrialsoft_id(trialsoft_id);
        trialsoftdetailMapper.delete(tail);
        List<Trialsoftdetail> trialsoftdetaillist = trialsoftVo.getTrialsoftdetail();
        if (trialsoftdetaillist != null) {
            int rowindex = 0;
            for (Trialsoftdetail trialsoftdetail : trialsoftdetaillist) {
                rowindex = rowindex + 1;
                trialsoftdetail.preInsert(tokenModel);
                trialsoftdetail.setTrialsoftdetail_id(UUID.randomUUID().toString());
                trialsoftdetail.setTrialsoft_id(trialsoft_id);
                trialsoftdetail.setRowindex(rowindex);
                trialsoftdetailMapper.insertSelective(trialsoftdetail);
            }
        }
    }
    @Override
    public void insert(TrialsoftVo trialsoftVo, TokenModel tokenModel) throws Exception {
        String trialsoft_id = UUID.randomUUID().toString();
        Trialsoft trialsoft = new Trialsoft();
        BeanUtils.copyProperties(trialsoftVo.getTrialsoft(), trialsoft);
        trialsoft.preInsert(tokenModel);
        trialsoft.setTrialsoft_id(trialsoft_id);
        trialsoftMapper.insertSelective(trialsoft);
        List<Trialsoftdetail> trialsoftdetaillist = trialsoftVo.getTrialsoftdetail();
        if (trialsoftdetaillist != null) {
            int rowindex = 0;
            for (Trialsoftdetail trialsoftdetail : trialsoftdetaillist) {
                rowindex = rowindex + 1;
                trialsoftdetail.preInsert(tokenModel);
                trialsoftdetail.setTrialsoftdetail_id(UUID.randomUUID().toString());
                trialsoftdetail.setTrialsoft_id(trialsoft_id);
                trialsoftdetail.setRowindex(rowindex);
                trialsoftdetail.setMachinename(trialsoftdetail.getMachinename());
                trialsoftdetail.setCustomer(trialsoftdetail.getCustomer());
                trialsoftdetail.setStartdate(trialsoftdetail.getStartdate());
                trialsoftdetail.setEnddate(trialsoftdetail.getEnddate());
                trialsoftdetail.setSoftwarename(trialsoftdetail.getSoftwarename());
                trialsoftdetail.setNature(trialsoftdetail.getNature());
                trialsoftdetail.setDeveloper(trialsoftdetail.getDeveloper());
                trialsoftdetail.setEmploy(trialsoftdetail.getEmploy());
                trialsoftdetail.setSofttype(trialsoftdetail.getSofttype());
                trialsoftdetailMapper.insertSelective(trialsoftdetail);
            }
        }
    }


}
