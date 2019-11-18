package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.OtherDetails;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS1000.PurchaseDetails;
import com.nt.dao_Pfans.PFANS1000.TrafficDetails;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.OtherDetailsMapper;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
import com.nt.service_pfans.PFANS1000.mapper.PurchaseDetailsMapper;
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

    @Autowired
    private PurchaseDetailsMapper purchaseDetailsMapper;

    @Autowired
    private OtherDetailsMapper otherDetailsMapper;

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
        List<PurchaseDetails> purchaseDetailslist=publicExpenseVo.getPurchaseDetails();
        List<OtherDetails> otherDetailslist=publicExpenseVo.getOtherDetails();
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
        if (purchaseDetailslist != null) {
            int rowundex = 0;
            for (PurchaseDetails purchaseDetails : purchaseDetailslist) {
                rowundex = rowundex + 1;
                purchaseDetails.preInsert(tokenModel);
                purchaseDetails.setPurchasedetails_id(UUID.randomUUID().toString());
                purchaseDetails.setPublicexpense_id(publicexpenseid);
                purchaseDetails.setRowindex(rowundex);
                purchaseDetailsMapper.insertSelective(purchaseDetails);
            }
        }
        if (otherDetailslist != null) {
            int rowundex = 0;
            for (OtherDetails otherDetails : otherDetailslist) {
                rowundex = rowundex + 1;
                otherDetails.preInsert(tokenModel);
                otherDetails.setOtherdetails_id(UUID.randomUUID().toString());
                otherDetails.setPublicexpense_id(publicexpenseid);
                otherDetails.setRowindex(rowundex);
                otherDetailsMapper.insertSelective(otherDetails);
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

        PurchaseDetails purchase=new PurchaseDetails();
        purchase.setPurchasedetails_id(spublicexpenseid);
        purchaseDetailsMapper.delete(purchase);
        List<PurchaseDetails> purchaselist=publicExpenseVo.getPurchaseDetails();

        OtherDetails other=new OtherDetails();
        other.setOtherdetails_id(spublicexpenseid);
        otherDetailsMapper.delete(other);
        List<OtherDetails> otherlist=publicExpenseVo.getOtherDetails();

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
        if (purchaselist != null) {
            int rowundex = 0;
            for (PurchaseDetails purchaseDetails : purchaselist) {
                rowundex = rowundex + 1;
                purchaseDetails.preInsert(tokenModel);
                purchaseDetails.setPurchasedetails_id(UUID.randomUUID().toString());
                purchaseDetails.setPublicexpense_id(spublicexpenseid);
                purchaseDetails.setRowindex(rowundex);
                purchaseDetailsMapper.insertSelective(purchaseDetails);
            }
        }
        if (otherlist != null) {
            int rowundex = 0;
            for (OtherDetails otherDetails : otherlist) {
                rowundex = rowundex + 1;
                otherDetails.preInsert(tokenModel);
                otherDetails.setOtherdetails_id(UUID.randomUUID().toString());
                otherDetails.setPublicexpense_id(spublicexpenseid);
                otherDetails.setRowindex(rowundex);
                otherDetailsMapper.insertSelective(otherDetails);
            }
        }

    }

    //按id查询

    @Override
    public PublicExpenseVo selectById(String publicexpenseid) throws Exception {
        PublicExpenseVo pubVo = new PublicExpenseVo();
        TrafficDetails trafficDetails = new TrafficDetails();
        PurchaseDetails purchaseDetails=new PurchaseDetails();
        OtherDetails otherDetails=new OtherDetails();
        trafficDetails.setTrafficdetails_id(publicexpenseid);
        purchaseDetails.setPurchasedetails_id(publicexpenseid);
        otherDetails.setOtherdetails_id(publicexpenseid);
        List<TrafficDetails> trafficDetailslist = trafficDetailsMapper.select(trafficDetails);
        List<PurchaseDetails> purchaseDetailslist =purchaseDetailsMapper.select(purchaseDetails);
        List<OtherDetails> otherDetailslist=otherDetailsMapper.select(otherDetails);
        trafficDetailslist = trafficDetailslist.stream().sorted(Comparator.comparing(TrafficDetails::getRowindex)).collect(Collectors.toList());
        purchaseDetailslist=purchaseDetailslist.stream().sorted(Comparator.comparing(PurchaseDetails::getAnnexno)).collect(Collectors.toList());
        otherDetailslist=otherDetailslist.stream().sorted(Comparator.comparing(OtherDetails::getAnnexno)).collect(Collectors.toList());
        PublicExpense pub = publicExpenseMapper.selectByPrimaryKey(publicexpenseid);
        pubVo.setPublicExpense(pub);
        pubVo.setTrafficDetails(trafficDetailslist);
        pubVo.setPurchaseDetails(purchaseDetailslist);
        pubVo.setOtherDetails(otherDetailslist);
        return pubVo;
    }

}
