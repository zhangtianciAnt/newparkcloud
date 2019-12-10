package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Outside;
import com.nt.dao_Pfans.PFANS1000.Outsidedetail;
import com.nt.dao_Pfans.PFANS1000.Vo.OutsideVo;
import com.nt.service_pfans.PFANS1000.OutsideService;
import com.nt.service_pfans.PFANS1000.mapper.OutsideMapper;
import com.nt.service_pfans.PFANS1000.mapper.OutsidedetailMapper;
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
public class OutsideServiceImpl implements OutsideService {

    @Autowired
    private OutsideMapper outsideMapper;

    @Autowired
    private OutsidedetailMapper outsidedetailMapper;

    @Override
    public List<Outside> get(Outside outside)  throws Exception{
        return outsideMapper.select(outside);
    }

    @Override
    public OutsideVo selectById(String outsideid) throws Exception {
        OutsideVo asseVo = new OutsideVo();
        Outsidedetail outsidedetail = new Outsidedetail();
        outsidedetail.setOutsideid(outsideid);
        List<Outsidedetail> outsidedetaillist = outsidedetailMapper.select(outsidedetail);
        outsidedetaillist = outsidedetaillist.stream().sorted(Comparator.comparing(Outsidedetail::getRowindex)).collect(Collectors.toList());
        Outside out = outsideMapper.selectByPrimaryKey(outsideid);
        asseVo.setOutside(out);
        asseVo.setOutsidedetail(outsidedetaillist);
        return asseVo;
    }

    @Override
    public void update(OutsideVo outsideVo, TokenModel tokenModel) throws Exception {
        Outside outside = new Outside();
        BeanUtils.copyProperties(outsideVo.getOutside(), outside);
        outside.preUpdate(tokenModel);
        outsideMapper.updateByPrimaryKey(outside);
        String soutsideid = outside.getOutsideid();
        Outsidedetail tail = new Outsidedetail();
        tail.setOutsideid(soutsideid);
        outsidedetailMapper.delete(tail);
        List<Outsidedetail> outsidedetaillist = outsideVo.getOutsidedetail();
        if (outsidedetaillist != null) {
            int rowindex = 0;
            for (Outsidedetail outsidedetail : outsidedetaillist) {
                rowindex = rowindex + 1;
                outsidedetail.preInsert(tokenModel);
                outsidedetail.setOutsidedetailid(UUID.randomUUID().toString());
                outsidedetail.setOutsideid(soutsideid);
                outsidedetail.setRowindex(rowindex);
                outsidedetailMapper.insertSelective(outsidedetail);
            }
        }
    }
    @Override
    public void insert(OutsideVo outsideVo, TokenModel tokenModel) throws Exception {
        String outsideid = UUID.randomUUID().toString();
        Outside outside = new Outside();
        BeanUtils.copyProperties(outsideVo.getOutside(), outside);
        outside.preInsert(tokenModel);
        outside.setOutsideid(outsideid);
        outsideMapper.insertSelective(outside);
        List<Outsidedetail> outsidedetaillist = outsideVo.getOutsidedetail();
        if (outsidedetaillist != null) {
            int rowindex = 0;
            for (Outsidedetail outsidedetail : outsidedetaillist) {
                rowindex = rowindex + 1;
                outsidedetail.preInsert(tokenModel);
                outsidedetail.setOutsidedetailid(UUID.randomUUID().toString());
                outsidedetail.setOutsideid(outsideid);
                outsidedetail.setRowindex(rowindex);
                outsidedetailMapper.insertSelective(outsidedetail);
            }
        }
    }
}
