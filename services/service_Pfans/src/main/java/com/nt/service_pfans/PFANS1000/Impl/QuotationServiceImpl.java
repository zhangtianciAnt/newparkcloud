package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.QuotationVo;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS1000.QuotationService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.ExcelOutPutUtil;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
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

    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

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
        List<Personfee> personfeelist = personfeeMapper.select(personfee);
        List<Othpersonfee> othpersonfeelist = othpersonfeeMapper.select(othpersonfee);
        List<Fruit> fruitlist = fruitMapper.select(fruit);
        personfeelist = personfeelist.stream().sorted(Comparator.comparing(Personfee::getRowindex)).collect(Collectors.toList());
        othpersonfeelist = othpersonfeelist.stream().sorted(Comparator.comparing(Othpersonfee::getRowindex)).collect(Collectors.toList());
        fruitlist = fruitlist.stream().sorted(Comparator.comparing(Fruit::getRowindex)).collect(Collectors.toList());
        Quotation quo = quotationMapper.selectByPrimaryKey(quotationid);
        Quotation quotation = quotationMapper.selectByPrimaryKey(quotationid);
        String name = "";
        String [] companyProjectsid = quotation.getPjchinese().split(",");
        if(companyProjectsid.length > 0){
            for (int i = 0;i < companyProjectsid.length;i++){
                CompanyProjects companyProjects = new CompanyProjects();
                companyProjects.setCompanyprojects_id(companyProjectsid[i]);
                List<CompanyProjects> comList = companyProjectsMapper.select(companyProjects);
                if(comList.size() > 0){
                    name = name + comList.get(0).getProject_name() + ",";
                }
            }
            if(!name.equals("")){
                name = name.substring(0,name.length()-1);
            }
        }
        quotation.setPjchinese(name);
        asseVo.setQuotation(quo);
        asseVo.setPersonfee(personfeelist);
        asseVo.setOthpersonfee(othpersonfeelist);
        asseVo.setFruit(fruitlist);

        if ( asseVo != null ) {
            Contractnumbercount contractnumbercount = new Contractnumbercount();
            contractnumbercount.setContractnumber(quo.getContractnumber());
            List<Contractnumbercount> list = contractnumbercountMapper.select(contractnumbercount);
            asseVo.setNumbercounts(list);
        }

        return asseVo;
    }

//    @Override
//    public  List<Quotation> getquotation(QuotationVo quotationVo) throws Exception {
//        List<Quotation> listvo = new ArrayList<Quotation>();
//        List<Personfee> listvo1 = new ArrayList<Personfee>();
//        Quotation quotation = new Quotation();
//        List<Quotation> quotationlist = quotationVo.getQuotation();
//        for(Quotation quList:quotationlist){
//            quotation.setQuotationid(quList.getQuotationid());
//            List<Quotation> listVo = quotationMapper.select(quotation);
//            listVo = listVo.stream().sorted(Comparator.comparing(Quotation::getNumber)).collect(Collectors.toList());
//            listvo.addAll(0,listVo);
//        }
//
//        return listvo;
//    }

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

}
