package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.dao_Pfans.PFANS1000.Scrapdetails;
import com.nt.dao_Pfans.PFANS1000.Salesdetails;
import com.nt.dao_Pfans.PFANS1000.Vo.AssetinformationVo;
import com.nt.service_pfans.PFANS1000.AssetinformationService;
import com.nt.service_pfans.PFANS1000.mapper.AssetinformationMapper;
import com.nt.service_pfans.PFANS1000.mapper.ScrapdetailsMapper;
import com.nt.service_pfans.PFANS1000.mapper.SalesdetailsMapper;
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
public class AssetinformationServiceImpl implements AssetinformationService {

    @Autowired
    private AssetinformationMapper assetinformationMapper;

    @Autowired
    private ScrapdetailsMapper scrapdetailsMapper;

    @Autowired
    private SalesdetailsMapper salesdetailsMapper;

    @Override
    public List<Assetinformation> getAssetinformation(Assetinformation assetinformation)  throws Exception{
        return assetinformationMapper.select(assetinformation);
    }

    @Override
    public AssetinformationVo selectById(String assetinformationid) throws Exception {
        AssetinformationVo asseVo = new AssetinformationVo();
        Scrapdetails scrapdetails = new Scrapdetails();
        Salesdetails salesdetails = new Salesdetails();
        scrapdetails.setAssetinformationid(assetinformationid);
        salesdetails.setAssetinformationid(assetinformationid);
        List<Scrapdetails> scrapdetailslist = scrapdetailsMapper.select(scrapdetails);
        List<Salesdetails> salesdetailslist = salesdetailsMapper.select(salesdetails);
        scrapdetailslist = scrapdetailslist.stream().sorted(Comparator.comparing(Scrapdetails::getRowindex)).collect(Collectors.toList());
        salesdetailslist = salesdetailslist.stream().sorted(Comparator.comparing(Salesdetails::getRowindex)).collect(Collectors.toList());
        Assetinformation asse = assetinformationMapper.selectByPrimaryKey(assetinformationid);
        asseVo.setAssetinformation(asse);
        asseVo.setScrapdetails(scrapdetailslist);
        asseVo.setSalesdetails(salesdetailslist);
        return asseVo;
    }

    @Override
    public void updateAssetinformation(AssetinformationVo assetinformationVo, TokenModel tokenModel) throws Exception {
        Assetinformation assetinformation = new Assetinformation();
        BeanUtils.copyProperties(assetinformationVo.getAssetinformation(), assetinformation);
        assetinformation.preUpdate(tokenModel);
        assetinformationMapper.updateByPrimaryKey(assetinformation);
        String sassetinformationid = assetinformation.getAssetinformationid();
        Scrapdetails sce = new Scrapdetails();
        Salesdetails sal = new Salesdetails();
        sce.setAssetinformationid(sassetinformationid);
        sal.setAssetinformationid(sassetinformationid);
        scrapdetailsMapper.delete(sce);
        salesdetailsMapper.delete(sal);
        List<Scrapdetails> scrapdetailslist = assetinformationVo.getScrapdetails();
        List<Salesdetails> salesdetailslist = assetinformationVo.getSalesdetails();
        if (scrapdetailslist != null) {
            int rowindex = 0;
            for (Scrapdetails scrapdetails : scrapdetailslist) {
                rowindex = rowindex + 1;
                scrapdetails.preInsert(tokenModel);
                scrapdetails.setScrapdetailsid(UUID.randomUUID().toString());
                scrapdetails.setAssetinformationid(sassetinformationid);
                scrapdetails.setRowindex(rowindex);
                scrapdetailsMapper.insertSelective(scrapdetails);
            }
        }
        if (salesdetailslist != null) {
            int rowindex = 0;
            for (Salesdetails salesdetails : salesdetailslist) {
                rowindex = rowindex + 1;
                salesdetails.preInsert(tokenModel);
                salesdetails.setSalesdetailsid(UUID.randomUUID().toString());
                salesdetails.setAssetinformationid(sassetinformationid);
                salesdetails.setRowindex(rowindex);
                salesdetailsMapper.insertSelective(salesdetails);
            }
        }
    }
    @Override
    public void insert(AssetinformationVo assetinformationVo, TokenModel tokenModel) throws Exception {
        String assetinformationid = UUID.randomUUID().toString();
        Assetinformation assetinformation = new Assetinformation();
        BeanUtils.copyProperties(assetinformationVo.getAssetinformation(), assetinformation);
        assetinformation.preInsert(tokenModel);
        assetinformation.setAssetinformationid(assetinformationid);
        assetinformationMapper.insertSelective(assetinformation);
        List<Scrapdetails> scrapdetailslist = assetinformationVo.getScrapdetails();
        List<Salesdetails> salesdetailslist = assetinformationVo.getSalesdetails();
        if (scrapdetailslist != null) {
            int rowindex = 0;
            for (Scrapdetails scrapdetails : scrapdetailslist) {
                rowindex = rowindex + 1;
                scrapdetails.preInsert(tokenModel);
                scrapdetails.setScrapdetailsid(UUID.randomUUID().toString());
                scrapdetails.setAssetinformationid(assetinformationid);
                scrapdetails.setRowindex(rowindex);
                scrapdetailsMapper.insertSelective(scrapdetails);
            }
        }
        if (salesdetailslist != null) {
            int rowindex = 0;
            for (Salesdetails salesdetails : salesdetailslist) {
                rowindex = rowindex + 1;
                salesdetails.preInsert(tokenModel);
                salesdetails.setSalesdetailsid(UUID.randomUUID().toString());
                salesdetails.setAssetinformationid(assetinformationid);
                salesdetails.setRowindex(rowindex);
                salesdetailsMapper.insertSelective(salesdetails);
            }
        }
    }
}
