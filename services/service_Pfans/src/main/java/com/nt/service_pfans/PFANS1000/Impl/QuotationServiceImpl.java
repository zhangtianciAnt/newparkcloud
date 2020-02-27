package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Quotation;
import com.nt.dao_Pfans.PFANS1000.Basicinformation;
import com.nt.dao_Pfans.PFANS1000.Vo.QuotationVo;
import com.nt.service_pfans.PFANS1000.QuotationService;
import com.nt.service_pfans.PFANS1000.mapper.QuotationMapper;
import com.nt.service_pfans.PFANS1000.mapper.BasicinformationMapper;
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
public class QuotationServiceImpl implements QuotationService {

    @Autowired
    private QuotationMapper quotationMapper;

    @Autowired
    private BasicinformationMapper basicinformationMapper;

    @Override
    public List<Quotation> get(Quotation quotation)  throws Exception{
        return quotationMapper.select(quotation);
    }

    @Override
    public QuotationVo selectById(String quotationid) throws Exception {
        QuotationVo asseVo = new QuotationVo();
        Basicinformation basicinformation = new Basicinformation();
        basicinformation.setQuotationid(quotationid);
        List<Basicinformation> basicinformationlist = basicinformationMapper.select(basicinformation);
        basicinformationlist = basicinformationlist.stream().sorted(Comparator.comparing(Basicinformation::getRowindex)).collect(Collectors.toList());
        Quotation quo = quotationMapper.selectByPrimaryKey(quotationid);
        asseVo.setQuotation(quo);
        asseVo.setBasicinformation(basicinformationlist);
        return asseVo;
    }

    @Override
    public void update(QuotationVo quotationVo, TokenModel tokenModel) throws Exception {
        Quotation quotation = new Quotation();
        BeanUtils.copyProperties(quotationVo.getQuotation(), quotation);
        quotation.preUpdate(tokenModel);
        quotationMapper.updateByPrimaryKey(quotation);
        String sholidayid = quotation.getQuotationid();
        Basicinformation tail = new Basicinformation();
        tail.setQuotationid(sholidayid);
        basicinformationMapper.delete(tail);
        List<Basicinformation> basicinformationlist = quotationVo.getBasicinformation();
        if (basicinformationlist != null) {
            int rowindex = 0;
            for (Basicinformation basicinformation : basicinformationlist) {
                rowindex = rowindex + 1;
                basicinformation.preInsert(tokenModel);
                basicinformation.setBasicinformationid(UUID.randomUUID().toString());
                basicinformation.setQuotationid(sholidayid);
                basicinformation.setRowindex(rowindex);
                basicinformationMapper.insertSelective(basicinformation);
            }
        }
    }
    @Override
    public void insert(QuotationVo quotationVo, TokenModel tokenModel) throws Exception {
        String quotationid = UUID.randomUUID().toString();
        Quotation quotation = new Quotation();
        BeanUtils.copyProperties(quotationVo.getQuotation(), quotation);
        quotation.preInsert(tokenModel);
        quotation.setQuotationid(quotationid);
        quotationMapper.insertSelective(quotation);
        List<Basicinformation> basicinformationlist = quotationVo.getBasicinformation();
        if (basicinformationlist != null) {
            int rowindex = 0;
            for (Basicinformation basicinformation : basicinformationlist) {
                rowindex = rowindex + 1;
                basicinformation.preInsert(tokenModel);
                basicinformation.setBasicinformationid(UUID.randomUUID().toString());
                basicinformation.setQuotationid(quotationid);
                basicinformation.setRowindex(rowindex);
                basicinformationMapper.insertSelective(basicinformation);
            }
        }
    }
}
