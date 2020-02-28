package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Quotation;
import com.nt.dao_Pfans.PFANS1000.Basicinformation;
import com.nt.dao_Pfans.PFANS1000.Personfee;
import com.nt.dao_Pfans.PFANS1000.Othpersonfee;
import com.nt.dao_Pfans.PFANS1000.Fruit;
import com.nt.dao_Pfans.PFANS1000.Vo.QuotationVo;
import com.nt.service_pfans.PFANS1000.QuotationService;
import com.nt.service_pfans.PFANS1000.mapper.QuotationMapper;
import com.nt.service_pfans.PFANS1000.mapper.BasicinformationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PersonfeeMapper;
import com.nt.service_pfans.PFANS1000.mapper.OthpersonfeeMapper;
import com.nt.service_pfans.PFANS1000.mapper.FruitMapper;
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

    @Autowired
    private PersonfeeMapper personfeeMapper;

    @Autowired
    private OthpersonfeeMapper othpersonfeeMapper;

    @Autowired
    private FruitMapper fruitMapper;

    @Override
    public List<Quotation> get(Quotation quotation)  throws Exception{
        return quotationMapper.select(quotation);
    }

    @Override
    public QuotationVo selectById(String quotationid) throws Exception {
        QuotationVo asseVo = new QuotationVo();
        Basicinformation basicinformation = new Basicinformation();
        Personfee personfee = new Personfee();
        Othpersonfee othpersonfee = new Othpersonfee();
        Fruit fruit = new Fruit();
        basicinformation.setQuotationid(quotationid);
        personfee.setQuotationid(quotationid);
        othpersonfee.setQuotationid(quotationid);
        fruit.setQuotationid(quotationid);
        List<Basicinformation> basicinformationlist = basicinformationMapper.select(basicinformation);
        List<Personfee> personfeelist = personfeeMapper.select(personfee);
        List<Othpersonfee> othpersonfeelist = othpersonfeeMapper.select(othpersonfee);
        List<Fruit> fruitlist = fruitMapper.select(fruit);
        basicinformationlist = basicinformationlist.stream().sorted(Comparator.comparing(Basicinformation::getRowindex)).collect(Collectors.toList());
        personfeelist = personfeelist.stream().sorted(Comparator.comparing(Personfee::getRowindex)).collect(Collectors.toList());
        othpersonfeelist = othpersonfeelist.stream().sorted(Comparator.comparing(Othpersonfee::getRowindex)).collect(Collectors.toList());
        fruitlist = fruitlist.stream().sorted(Comparator.comparing(Fruit::getRowindex)).collect(Collectors.toList());
        Quotation quo = quotationMapper.selectByPrimaryKey(quotationid);
        asseVo.setQuotation(quo);
        asseVo.setBasicinformation(basicinformationlist);
        asseVo.setPersonfee(personfeelist);
        asseVo.setOthpersonfee(othpersonfeelist);
        asseVo.setFruit(fruitlist);
        return asseVo;
    }

    @Override
    public void update(QuotationVo quotationVo, TokenModel tokenModel) throws Exception {
        Quotation quotation = new Quotation();
        BeanUtils.copyProperties(quotationVo.getQuotation(), quotation);
        quotation.preUpdate(tokenModel);
        quotationMapper.updateByPrimaryKey(quotation);
        String squotationid = quotation.getQuotationid();
        Basicinformation tail = new Basicinformation();
        Personfee per = new Personfee();
        Othpersonfee oth = new Othpersonfee();
        Fruit fru = new Fruit();
        tail.setQuotationid(squotationid);
        basicinformationMapper.delete(tail);
        personfeeMapper.delete(per);
        othpersonfeeMapper.delete(oth);
        fruitMapper.delete(fru);
        List<Basicinformation> basicinformationlist = quotationVo.getBasicinformation();
        List<Personfee> personfeelist = quotationVo.getPersonfee();
        List<Othpersonfee> othpersonfeelist = quotationVo.getOthpersonfee();
        List<Fruit> fruitlist = quotationVo.getFruit();
        if (basicinformationlist != null) {
            int rowindex = 0;
            for (Basicinformation basicinformation : basicinformationlist) {
                rowindex = rowindex + 1;
                basicinformation.preInsert(tokenModel);
                basicinformation.setBasicinformationid(UUID.randomUUID().toString());
                basicinformation.setQuotationid(squotationid);
                basicinformation.setRowindex(rowindex);
                basicinformationMapper.insertSelective(basicinformation);
            }
        }
        if (personfeelist != null) {
            int rowindex = 0;
            for (Personfee personfee : personfeelist) {
                rowindex = rowindex + 1;
                personfee.preInsert(tokenModel);
                personfee.setPersonfeeid(UUID.randomUUID().toString());
                personfee.setQuotationid(squotationid);
                personfee.setRowindex(rowindex);
                personfeeMapper.insertSelective(personfee);
            }
        }
        if (othpersonfeelist != null) {
            int rowindex = 0;
            for (Othpersonfee othpersonfee : othpersonfeelist) {
                rowindex = rowindex + 1;
                othpersonfee.preInsert(tokenModel);
                othpersonfee.setOthpersonfeeid(UUID.randomUUID().toString());
                othpersonfee.setQuotationid(squotationid);
                othpersonfee.setRowindex(rowindex);
                othpersonfeeMapper.insertSelective(othpersonfee);
            }
        }
        if (fruitlist != null) {
            int rowindex = 0;
            for (Fruit fruit : fruitlist) {
                rowindex = rowindex + 1;
                fruit.preInsert(tokenModel);
                fruit.setFruitid(UUID.randomUUID().toString());
                fruit.setQuotationid(squotationid);
                fruit.setRowindex(rowindex);
                fruitMapper.insertSelective(fruit);
            }
        }
    }
//    @Override
//    public void insert(QuotationVo quotationVo, TokenModel tokenModel) throws Exception {
//        String quotationid = UUID.randomUUID().toString();
//        Quotation quotation = new Quotation();
//        BeanUtils.copyProperties(quotationVo.getQuotation(), quotation);
//        quotation.preInsert(tokenModel);
//        quotation.setQuotationid(quotationid);
//        quotationMapper.insertSelective(quotation);
//        List<Basicinformation> basicinformationlist = quotationVo.getBasicinformation();
//        if (basicinformationlist != null) {
//            int rowindex = 0;
//            for (Basicinformation basicinformation : basicinformationlist) {
//                rowindex = rowindex + 1;
//                basicinformation.preInsert(tokenModel);
//                basicinformation.setBasicinformationid(UUID.randomUUID().toString());
//                basicinformation.setQuotationid(quotationid);
//                basicinformation.setRowindex(rowindex);
//                basicinformationMapper.insertSelective(basicinformation);
//            }
//        }
//    }
}
