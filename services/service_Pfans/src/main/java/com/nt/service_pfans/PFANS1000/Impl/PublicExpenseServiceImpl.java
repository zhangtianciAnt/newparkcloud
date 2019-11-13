package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS1000.TrafficDetails;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
import com.nt.service_pfans.PFANS1000.mapper.TrafficDetailsMapper;
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
public class PublicExpenseServiceImpl implements PublicExpenseService {


    @Autowired
    private PublicExpenseMapper publicExpenseMapper;

    @Autowired
    private TrafficDetailsMapper trafficDetailsMapper;

    //列表查询
    @Override
    public List<PublicExpense> get(PublicExpense publicExpense) throws Exception {
        return publicExpenseMapper.select(publicExpense);
    }

    //新建
    @Override
    public void insert(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception {
        String publicexpenseid = UUID.randomUUID().toString();
        PublicExpense publicExpense = new PublicExpense();
        BeanUtils.copyProperties(publicExpenseVo.getPublicExpense(),publicExpense);
        publicExpense.preInsert(tokenModel);
        publicExpense.setPublicexpenseid(publicexpenseid);
        publicExpenseMapper.insertSelective(publicExpense);
        List<TrafficDetails> trafficDetailslist = publicExpenseVo.getTrafficDetails();
        if (trafficDetailslist != null) {
            int rowundex = 0;
            for (TrafficDetails trafficDetails : trafficDetailslist) {
                rowundex = rowundex + 1;
                trafficDetails.preInsert(tokenModel);
                trafficDetails.setTrafficdetails_id(UUID.randomUUID().toString());
                trafficDetails.setPublicexpense_id(publicexpenseid);
                trafficDetails.setRowindex(rowundex);
                trafficDetailsMapper.insertSelective(trafficDetails);
            }
        }
    }

    //编辑
    @Override
    public void update(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception {
        PublicExpense publicExpense = new PublicExpense();
        BeanUtils.copyProperties(publicExpenseVo.getPublicExpense(),publicExpense);
        publicExpense.preUpdate(tokenModel);
        publicExpenseMapper.updateByPrimaryKey(publicExpense);
        String spublicexpenseid = publicExpense.getPublicexpenseid();
        TrafficDetails traffic = new TrafficDetails();
        traffic.setPublicexpense_id(spublicexpenseid);
        trafficDetailsMapper.delete(traffic);
        List<TrafficDetails> trafficlist = publicExpenseVo.getTrafficDetails();
        if (trafficlist != null) {
            int rowundex = 0;
            for (TrafficDetails trafficDetails : trafficlist) {
                rowundex = rowundex + 1;
                trafficDetails.preInsert(tokenModel);
                trafficDetails.setTrafficdetails_id(UUID.randomUUID().toString());
                trafficDetails.setPublicexpense_id(spublicexpenseid);
                trafficDetails.setRowindex(rowundex);
                trafficDetailsMapper.insertSelective(trafficDetails);
            }
        }
    }

    //按id查询

    @Override
    public PublicExpenseVo selectById(String publicexpenseid) throws Exception {
        PublicExpenseVo pubVo = new PublicExpenseVo();
        TrafficDetails trafficDetails = new TrafficDetails();
        trafficDetails.setTrafficdetails_id(publicexpenseid);
        List<TrafficDetails> trafficDetailslist = trafficDetailsMapper.select(trafficDetails);
        trafficDetailslist = trafficDetailslist.stream().sorted(Comparator.comparing(TrafficDetails::getRowindex)).collect(Collectors.toList());
        PublicExpense pub = publicExpenseMapper.selectByPrimaryKey(publicexpenseid);
        pubVo.setPublicExpense(pub);
        pubVo.setTrafficDetails(trafficDetailslist);
        return pubVo;
    }
}
