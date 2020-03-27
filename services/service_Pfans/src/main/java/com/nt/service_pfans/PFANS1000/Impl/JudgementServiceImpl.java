package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.Unusedevice;
import com.nt.dao_Pfans.PFANS1000.Vo.JudgementVo;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.service_pfans.PFANS1000.JudgementService;
import com.nt.service_pfans.PFANS1000.mapper.JudgementMapper;
import com.nt.service_pfans.PFANS1000.mapper.UnusedeviceMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class JudgementServiceImpl implements JudgementService {

    @Autowired
    private JudgementMapper judgementMapper;

    @Autowired
    private UnusedeviceMapper unusedeviceMapper;

    @Override
    public List<Judgement> getJudgement(Judgement judgement ) {
        return judgementMapper.select(judgement);
    }

    @Override
    public List<Judgement> selectJudgement() throws Exception {
        return judgementMapper.selectJudgement();
    }

    @Override
    public JudgementVo One(String judgementid) throws Exception {
        JudgementVo judgVo = new JudgementVo();
        //设备
        Unusedevice unusedevice = new Unusedevice();

        unusedevice.setJudgementid(judgementid);

        Judgement judgement = judgementMapper.selectByPrimaryKey(judgementid);

        List<Unusedevice> unusedeviceList = unusedeviceMapper.select(unusedevice);
        unusedeviceList = unusedeviceList.stream().sorted(Comparator.comparing(Unusedevice::getRowindex)).collect(Collectors.toList());
        judgVo.setJudgement(judgement);
        judgVo.setUnusedevice(unusedeviceList);
        return judgVo;
    }

    @Override
    public void updateJudgement(JudgementVo judgementVo, TokenModel tokenModel) throws Exception {
        Judgement judgement = new Judgement();
        BeanUtils.copyProperties(judgementVo.getJudgement(), judgement);
        judgement.preUpdate(tokenModel);
        judgementMapper.updateByPrimaryKeySelective(judgement);
        String judgementid = judgement.getJudgementid();
        List<Unusedevice> unusedeviceList = judgementVo.getUnusedevice();
        if(unusedeviceList != null){
            Unusedevice unusedevice = new Unusedevice();
            unusedevice.setJudgementid(judgementid);
            unusedeviceMapper.delete(unusedevice);
            int rowindex = 0;
            for(Unusedevice unu : unusedeviceList){
                rowindex = rowindex + 1;
                unu.preInsert(tokenModel);
                unu.setUnusedeviceid(UUID.randomUUID().toString());
                unu.setJudgementid(judgementid);
                unu.setRowindex(rowindex);
                unusedeviceMapper.insertSelective(unu);
            }
        }

    }

    @Override
    public void insert(JudgementVo judgementVo, TokenModel tokenModel) throws Exception {
        String judgementid = UUID.randomUUID().toString();
        Judgement judgement = new Judgement();
        BeanUtils.copyProperties(judgementVo.getJudgement(), judgement);
        judgement.preInsert(tokenModel);
        judgement.setJudgementid(judgementid);
//        judgement.setEquipment("0");
        judgementMapper.insertSelective(judgement);
        List<Unusedevice> unusedeviceList = judgementVo.getUnusedevice();
        if(unusedeviceList != null){
//            judgement.setEquipment("1");
            int rowundex = 0;
            for(Unusedevice unu : unusedeviceList){
                rowundex = rowundex + 1;
                unu.preInsert(tokenModel);
                unu.setUnusedeviceid(UUID.randomUUID().toString());
                unu.setJudgementid(judgementid);
                unu.setRowindex(rowundex);
                unusedeviceMapper.insertSelective(unu);
            }
        }
    }

    @Override
    public List<Judgement> getJudgementList(Judgement judgement, HttpServletRequest request) throws Exception {

        return judgementMapper.select(judgement) ;
    }
}
