package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.Businessplandet;
import com.nt.dao_Pfans.PFANS1000.Pieceworktotal;
import com.nt.dao_Pfans.PFANS1000.Totalplan;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessplanVo;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.mapper.BusinessplanMapper;
import com.nt.service_pfans.PFANS1000.mapper.BusinessplandetMapper;
import com.nt.service_pfans.PFANS1000.mapper.PieceworktotalMapper;
import com.nt.service_pfans.PFANS1000.mapper.TotalplanMapper;
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
public class BusinessplanServiceImpl implements BusinessplanService {

    @Autowired
    private BusinessplanMapper businessplanMapper;
    @Autowired
    private TotalplanMapper totalplanMapper;
    @Autowired
    private PieceworktotalMapper pieceworktotalMapper;
    @Autowired
    private BusinessplandetMapper businessplandetMapper;

    @Override
    public List<Businessplan> get(Businessplan businessplan) throws Exception {
        return businessplanMapper.select(businessplan);
    }

    @Override
    public BusinessplanVo selectById(String businessplanid) throws Exception {
        BusinessplanVo busVo = new BusinessplanVo();

        Pieceworktotal pieceworktotal = new Pieceworktotal();
        Totalplan totalplan = new Totalplan();
        Businessplandet businessplandet=new Businessplandet();

        pieceworktotal.setBusinessplanid(businessplanid);
        totalplan.setBusinessplanid(businessplanid);
        businessplandet.setBusinessplan_id(businessplanid);

        List<Pieceworktotal> pieceworktotallist = pieceworktotalMapper.select(pieceworktotal);
        List<Totalplan> totalplanlist = totalplanMapper.select(totalplan);
        List<Businessplandet> businessplandetList = businessplandetMapper.select(businessplandet);

        pieceworktotallist = pieceworktotallist.stream().sorted(Comparator.comparing(Pieceworktotal::getRowindex)).collect(Collectors.toList());
        totalplanlist = totalplanlist.stream().sorted(Comparator.comparing(Totalplan::getRowindex)).collect(Collectors.toList());
        Businessplan Bus = businessplanMapper.selectByPrimaryKey(businessplanid);
        busVo.setBusinessplan(Bus);
        busVo.setPieceworktotal(pieceworktotallist);
        busVo.setTotalplan(totalplanlist);
        busVo.setBusinessplandets(businessplandetList);

        return busVo;
    }

    @Override
    public void updateBusinessplanVo(BusinessplanVo businessplanVo, TokenModel tokenModel) throws Exception {
        Businessplan businessplan = new Businessplan();
        BeanUtils.copyProperties(businessplanVo.getBusinessplan(), businessplan);
        businessplan.preUpdate(tokenModel);
        businessplanMapper.updateByPrimaryKey(businessplan);
        String businessplanid = businessplan.getBusinessplanid();
        Pieceworktotal piece = new Pieceworktotal();
        Totalplan total = new Totalplan();
        piece.setBusinessplanid(businessplanid);
        total.setBusinessplanid(businessplanid);
        pieceworktotalMapper.delete(piece);
        totalplanMapper.delete(total);
        List<Pieceworktotal> pieceworktotallist = businessplanVo.getPieceworktotal();
        List<Totalplan> totalplanlist = businessplanVo.getTotalplan();
        if (pieceworktotallist != null) {
            int rowindex = 0;
            for (Pieceworktotal pieceworktotal : pieceworktotallist) {
                rowindex = rowindex + 1;
                pieceworktotal.preInsert(tokenModel);
                pieceworktotal.setPieceworktotal_id(UUID.randomUUID().toString());
                pieceworktotal.setBusinessplanid(businessplanid);
                pieceworktotal.setRowindex(rowindex);
                pieceworktotalMapper.insertSelective(pieceworktotal);
            }
        }
        if (totalplanlist != null) {
            int rowindex = 0;
            for (Totalplan totalplan : totalplanlist) {
                rowindex = rowindex + 1;
                totalplan.preInsert(tokenModel);
                totalplan.setTotalplan_id(UUID.randomUUID().toString());
                totalplan.setBusinessplanid(businessplanid);
                totalplan.setRowindex(rowindex);
                totalplanMapper.insertSelective(totalplan);
            }
        }
    }

    @Override
    public void insertBusinessplanVo(BusinessplanVo businessplanVo, TokenModel tokenModel) throws Exception {
        String businessplanid = UUID.randomUUID().toString();
        Businessplan businessplan = new Businessplan();
        BeanUtils.copyProperties(businessplanVo.getBusinessplan(), businessplan);
        businessplan.preInsert(tokenModel);
        businessplan.setBusinessplanid(businessplanid);
        businessplanMapper.insertSelective(businessplan);
        List<Pieceworktotal> pieceworktotallist = businessplanVo.getPieceworktotal();
        List<Totalplan> totalplanlist = businessplanVo.getTotalplan();
        List<Businessplandet> businessplandetList = businessplanVo.getBusinessplandets();
        if(businessplandetList !=null){
            for(Businessplandet businessplandet : businessplandetList){
                businessplandet.setBusinessplandet_id(UUID.randomUUID().toString());
                businessplandet.setBusinessplan_id(businessplanid);
                businessplandetMapper.insertSelective(businessplandet);
            }
        }
        if (pieceworktotallist != null) {
            int rowindex = 0;
            for (Pieceworktotal pieceworktotal : pieceworktotallist) {
                rowindex = rowindex + 1;
                pieceworktotal.preInsert(tokenModel);
                pieceworktotal.setPieceworktotal_id(UUID.randomUUID().toString());
                pieceworktotal.setBusinessplanid(businessplanid);
                pieceworktotal.setRowindex(rowindex);
                pieceworktotalMapper.insertSelective(pieceworktotal);
            }
        }
        if (totalplanlist != null) {
            int rowindex = 0;
            for (Totalplan totalplan : totalplanlist) {
                rowindex = rowindex + 1;
                totalplan.preInsert(tokenModel);
                totalplan.setTotalplan_id(UUID.randomUUID().toString());
                totalplan.setBusinessplanid(businessplanid);
                totalplan.setRowindex(rowindex);
                totalplanMapper.insertSelective(totalplan);
            }
        }
    }
}
