package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.EntrustSupport;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
import com.nt.service_pfans.PFANS6000.EntrustSupportService;
import com.nt.service_pfans.PFANS6000.mapper.CompanyStatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.EntrustSupportMapper;
import com.nt.service_pfans.PFANS6000.mapper.SupplierinforMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class EntrustSupportServiceImpl implements EntrustSupportService {

    @Autowired
    private EntrustSupportMapper entrustSupportMapper;

    @Autowired
    private CompanyStatisticsMapper companyStatisticsMapper;

    @Autowired
    private SupplierinforMapper supplierinforMapper;

    @Autowired
    private WorkflowinstanceMapper workflowinstanceMapper;

    @Override
    public List<EntrustSupport> getList(EntrustSupport entrustSupport) throws Exception {
        List<EntrustSupport> res = entrustSupportMapper.select(entrustSupport);
        return res;
    }

    @Override
    public void updList(List<EntrustSupport> entrustSupport, TokenModel tokenModel) throws Exception {
        //region 更新请负一览 from
        String ym = entrustSupport.get(0).getDates();//本次数据的年月
        String years = ym.trim().split("-")[0];//本次数据的年月
        if(Integer.valueOf(ym.trim().split("-")[1]) < 4)
        {
            years = String.valueOf(Integer.valueOf(ym.trim().split("-")[0]) - 1);
        }
        years = years + "-" + ym.trim().split("-")[1];

        ArrayList<EntrustSupport> listOfBp = new ArrayList<>();//要更新到BP社的请负
        entrustSupport.forEach(item ->{
            if("true".equals(item.getProcessing())){
                if("".equals(item.getUndertaker()) || item.getUndertaker() == null){
                    listOfBp.add(item);//状态已处理
                }
                item.setUndertaker(tokenModel.getUserId());
            }
            item.preUpdate(tokenModel);
        });
        entrustSupportMapper.updateList(entrustSupport);
        //endregion 跟新请负一览 to

        //region 获取以处理完的请负一览数据，并转换成BP社统计的数据形式 from
        List<CompanyStatistics> tempList = new ArrayList<>();
        if(listOfBp != null && listOfBp.size() > 0){
            TreeMap<String,List<EntrustSupport>> BPList =  listOfBp.stream().collect(Collectors.groupingBy(EntrustSupport :: getCustojapanese,TreeMap::new,Collectors.toList()));
            for (Map.Entry<String, List<EntrustSupport>> entry : BPList.entrySet()) {
                CompanyStatistics temp = new CompanyStatistics();
                temp.setBpcompany(entry.getKey());//公司名
                temp.setYear(years.trim().split("-")[0]);//年
                temp.setGroup_id(entry.getValue().get(0).getGroup_id());//部门
                BigDecimal sum = new BigDecimal(BigInteger.ZERO);//费用
                for(EntrustSupport entrust : entry.getValue()){
                    sum = sum.add(new BigDecimal(entrust.getClaimamount())).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                switch (years.trim().split("-")[1]){
                    case "01" :
                        temp.setCost1f(sum);
                        temp.setManhour1f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "02" :
                        temp.setCost2f(sum);
                        temp.setManhour2f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "03" :
                        temp.setCost3f(sum);
                        temp.setManhour3f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "04" :
                        temp.setCost4f(sum);
                        temp.setManhour4f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "05" :
                        temp.setCost5f(sum);
                        temp.setManhour5f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "06" :
                        temp.setCost6f(sum);
                        temp.setManhour6f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "07" :
                        temp.setCost7f(sum);
                        temp.setManhour7f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "08" :
                        temp.setCost8f(sum);
                        temp.setManhour8f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "09" :
                        temp.setCost9f(sum);
                        temp.setManhour9f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "10" :
                        temp.setCost10f(sum);
                        temp.setManhour10f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "11" :
                        temp.setCost11f(sum);
                        temp.setManhour11f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;
                    case "12" :
                        temp.setCost12f(sum);
                        temp.setManhour12f(sum.divide(new BigDecimal("18500"),2,BigDecimal.ROUND_HALF_UP));
                        break;

                }
                tempList.add(temp);
            }
        }
        //endregion 获取以处理完的请负一览数据，并转换成BP社统计的数据形式 to

        //region 更新BP from
        if (tempList.size() > 0 && tempList != null) {
            String tempmonth = "";
            for (CompanyStatistics com : tempList) {
                CompanyStatistics selectBycondition = new CompanyStatistics();
                selectBycondition.setYear(com.getYear());
                selectBycondition.setBpcompany(com.getBpcompany());
                selectBycondition.setGroup_id(com.getGroup_id());
                List<CompanyStatistics> res = companyStatisticsMapper.select(selectBycondition);//查询BP是否存在
                List<Workflowinstance> wL =new ArrayList<>();
                Workflowinstance w = new Workflowinstance();
                String dataid = "";
                dataid = com.getGroup_id() +","+years.trim().split("-")[0]+","+years.trim().split("-")[1];
                w.setDataid(dataid);
                wL = workflowinstanceMapper.select(w);
                if(wL.size()>0)
                {
                    wL = wL.stream().filter(item -> (item.getStatus().equals("4")) || item.getStatus().equals("0")).collect(Collectors.toList());
                    if(wL.size()>0)
                    {
                        tempmonth = tempmonth + Integer.valueOf(years.trim().split("-")[1]) + "月,";
                        continue;
                    }
                }
                if(res.size() > 0){
                    CompanyStatistics aSingle = res.get(0);//BP社
                    for(int i =1;i<=12;i++)
                    {
                        if(Integer.valueOf(years.trim().split("-")[1]) == i)
                        {
                            if(BeanUtils.getProperty(com,"cost"+ i +"f") != null && BeanUtils.getProperty(com,"manhour"+ i + "f") != null){//请负
                                BeanUtils.setProperty(aSingle,"cost"+ i +"f", new BigDecimal(BeanUtils.getProperty(res.get(0),"cost"+ i +"f")).add(new BigDecimal(BeanUtils.getProperty(com,"cost"+ i +"f"))));
                                BeanUtils.setProperty(aSingle,"manhour"+ i +"f", new BigDecimal(BeanUtils.getProperty(res.get(0),"manhour"+ i +"f")).add(new BigDecimal(BeanUtils.getProperty(com,"manhour"+ i +"f"))));
                            }
                        }
                    }

//                    if(com.getCost4f() != null && com.getManhour4f() != null){//请负4月
//                        aSingle.setCost4f(res.get(0).getCost4f().add(com.getCost4f()));
//                        aSingle.setManhour4f(res.get(0).getManhour4f().add(com.getManhour4f()));
//                    }
//                    if(com.getCost5f() != null && com.getManhour5f() != null){//请负5月
//                        aSingle.setCost5f(res.get(0).getCost5f().add(com.getCost5f()));
//                        aSingle.setManhour5f(res.get(0).getManhour5f().add(com.getManhour5f()));
//                    }
//                    if(com.getCost6f() != null && com.getManhour6f() != null){//请负6月
//                        aSingle.setCost6f(res.get(0).getCost6f().add(com.getCost6f()));
//                        aSingle.setManhour6f(res.get(0).getManhour6f().add(com.getManhour6f()));
//                    }
//                    if(com.getCost7f() != null && com.getManhour7f() != null){//请负7月
//                        aSingle.setCost7f(res.get(0).getCost7f().add(com.getCost7f()));
//                        aSingle.setManhour7f(res.get(0).getManhour7f().add(com.getManhour7f()));
//                    }
//                    if(com.getCost8f() != null && com.getManhour8f() != null){//请负8月
//                        aSingle.setCost8f(res.get(0).getCost8f().add(com.getCost8f()));
//                        aSingle.setManhour8f(res.get(0).getManhour8f().add(com.getManhour8f()));
//                    }
//                    if(com.getCost9f() != null && com.getManhour9f() != null){//请负9月
//                        aSingle.setCost9f(res.get(0).getCost9f().add(com.getCost9f()));
//                        aSingle.setManhour9f(res.get(0).getManhour9f().add(com.getManhour9f()));
//                    }
//                    if(com.getCost10f() != null && com.getManhour10f() != null){//请负10月
//                        aSingle.setCost10f(res.get(0).getCost10f().add(com.getCost10f()));
//                        aSingle.setManhour10f(res.get(0).getManhour10f().add(com.getManhour10f()));
//                    }
//                    if(com.getCost11f() != null && com.getManhour11f() != null){//请负11月
//                        aSingle.setCost11f(res.get(0).getCost11f().add(com.getCost11f()));
//                        aSingle.setManhour11f(res.get(0).getManhour11f().add(com.getManhour11f()));
//                    }
//                    if(com.getCost12f() != null && com.getManhour12f() != null){//请负12月
//                        aSingle.setCost12f(res.get(0).getCost12f().add(com.getCost12f()));
//                        aSingle.setManhour12f(res.get(0).getManhour12f().add(com.getManhour12f()));
//                    }
//                    if(com.getCost1f() != null && com.getManhour1f() != null){//请负1月
//                        aSingle.setCost1f(res.get(0).getCost1f().add(com.getCost1f()));
//                        aSingle.setManhour1f(res.get(0).getManhour1f().add(com.getManhour1f()));
//                    }
//                    if(com.getCost2f() != null && com.getManhour2f() != null){//请负2月
//                        aSingle.setCost2f(res.get(0).getCost2f().add(com.getCost2f()));
//                        aSingle.setManhour2f(res.get(0).getManhour2f().add(com.getManhour2f()));
//                    }
//                    if(com.getCost3f() != null && com.getManhour3f() != null){//请负3月
//                        aSingle.setCost3f(res.get(0).getCost3f().add(com.getCost3f()));
//                        aSingle.setManhour3f(res.get(0).getManhour3f().add(com.getManhour3f()));
//                    }
                    BigDecimal sumCost = aSingle.getCost4f().add(aSingle.getCost5f()).add(aSingle.getCost6f()).add(aSingle.getCost7f()).add(aSingle.getCost8f())
                            .add(aSingle.getCost9f()).add(aSingle.getCost10f()).add(aSingle.getCost11f()).add(aSingle.getCost12f())
                            .add(aSingle.getCost1f()).add(aSingle.getCost2f()).add(aSingle.getCost3f());
                    BigDecimal sumMan = aSingle.getManhour4f().add(aSingle.getManhour5f()).add(aSingle.getManhour6f()).add(aSingle.getManhour7f()).add(aSingle.getManhour8f())
                            .add(aSingle.getManhour9f()).add(aSingle.getManhour10f()).add(aSingle.getManhour11f()).add(aSingle.getManhour12f())
                            .add(aSingle.getManhour1f()).add(aSingle.getManhour2f()).add(aSingle.getManhour3f());
                    aSingle.setTotalcostf(sumCost);
                    aSingle.setTotalmanhourf(sumMan);
                    aSingle.preUpdate(tokenModel);
                    companyStatisticsMapper.updateByPrimaryKeySelective(aSingle);
                }
                else{
                    Supplierinfor supplierinfor = new Supplierinfor();//供应商信息
                    supplierinfor.setSupchinese(com.getBpcompany());//公司名
                    List<Supplierinfor> companynameWithId = supplierinforMapper.select(supplierinfor);//公司供应商信息
                    CompanyStatistics aSingle = new CompanyStatistics();//BP社
                    aSingle.setCompanystatistics_id(UUID.randomUUID().toString());//id
                    aSingle.setGroup_id(com.getGroup_id());
                    aSingle.setBpcompanyid(companynameWithId.get(0).getSupplierinfor_id());//外注公司id
                    aSingle.setBpcompany(com.getBpcompany());//公司名
                    aSingle.setYear(com.getYear());
//                    aSingle.setManhour4(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));//每月委任
//                    aSingle.setCost4(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour5(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost5(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour6(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost6(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour7(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost7(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour8(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost8(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour9(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost9(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour10(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost10(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour11(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost11(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour12(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost12(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour1(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost1(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour2(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost2(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setManhour3(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    aSingle.setCost3(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
                    aSingle.setTotalmanhour(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
                    aSingle.setTotalcost(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
                    for(int i =1;i<=12;i++)
                    {
                        BeanUtils.setProperty(aSingle,"cost"+ i, new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
                        BeanUtils.setProperty(aSingle,"manhour"+ i, new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
                        BeanUtils.setProperty(aSingle,"cost"+ i +"f", new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
                        BeanUtils.setProperty(aSingle,"manhour"+ i +"f", new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
                        if(Integer.valueOf(years.trim().split("-")[1]) == i)
                        {
                            if(BeanUtils.getProperty(com,"cost"+ i +"f") != null && BeanUtils.getProperty(com,"manhour"+ i + "f") != null){//请负
                                BeanUtils.setProperty(aSingle,"cost"+ i +"f", new BigDecimal(BeanUtils.getProperty(com,"cost"+ i +"f")));
                                BeanUtils.setProperty(aSingle,"manhour"+ i +"f", new BigDecimal(BeanUtils.getProperty(com,"manhour"+ i +"f")));
                            }
                        }
                    }
//                    if(com.getCost4f() != null && com.getManhour4f() != null){//请负4月
//                        aSingle.setCost4f(com.getCost4f());
//                        aSingle.setManhour4f(com.getManhour4f());
//                    }else{
//                        aSingle.setCost4f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour4f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost5f() != null && com.getManhour5f() != null){//请负5月
//                        aSingle.setCost5f(com.getCost5f());
//                        aSingle.setManhour5f(com.getManhour5f());
//                    }else{
//                        aSingle.setCost5f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour5f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost6f() != null && com.getManhour6f() != null){//请负6月
//                        aSingle.setCost6f(com.getCost6f());
//                        aSingle.setManhour6f(com.getManhour6f());
//                    }else{
//                        aSingle.setCost6f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour6f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost7f() != null && com.getManhour7f() != null){//请负7月
//                        aSingle.setCost7f(com.getCost7f());
//                        aSingle.setManhour7f(com.getManhour7f());
//                    }else{
//                        aSingle.setCost7f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour7f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost8f() != null && com.getManhour8f() != null){//请负8月
//                        aSingle.setCost8f(com.getCost8f());
//                        aSingle.setManhour8f(com.getManhour8f());
//                    }else{
//                        aSingle.setCost8f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour8f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost9f() != null && com.getManhour9f() != null){//请负9月
//                        aSingle.setCost9f(com.getCost9f());
//                        aSingle.setManhour9f(com.getManhour9f());
//                    }else{
//                        aSingle.setCost9f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour9f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost10f() != null && com.getManhour10f() != null){//请负10月
//                        aSingle.setCost10f(com.getCost10f());
//                        aSingle.setManhour10f(com.getManhour10f());
//                    }else{
//                        aSingle.setCost10f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour10f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost11f() != null && com.getManhour11f() != null){//请负11月
//                        aSingle.setCost11f(com.getCost11f());
//                        aSingle.setManhour11f(com.getManhour11f());
//                    }else{
//                        aSingle.setCost11f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour11f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost12f() != null && com.getManhour12f() != null){//请负12月
//                        aSingle.setCost12f(com.getCost12f());
//                        aSingle.setManhour12f(com.getManhour12f());
//                    }else{
//                        aSingle.setCost12f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour12f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost1f() != null && com.getManhour1f() != null){//请负1月
//                        aSingle.setCost1f(com.getCost1f());
//                        aSingle.setManhour1f(com.getManhour1f());
//                    }else{
//                        aSingle.setCost1f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour1f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost2f() != null && com.getManhour2f() != null){//请负2月
//                        aSingle.setCost2f(com.getCost2f());
//                        aSingle.setManhour2f(com.getManhour2f());
//                    }else{
//                        aSingle.setCost2f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour2f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
//                    if(com.getCost3f() != null && com.getManhour3f() != null){//请负5月
//                        aSingle.setCost3f(com.getCost3f());
//                        aSingle.setManhour3f(com.getManhour3f());
//                    }else{
//                        aSingle.setCost3f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                        aSingle.setManhour3f(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
                    BigDecimal sumCost = aSingle.getCost4f().add(aSingle.getCost5f()).add(aSingle.getCost6f()).add(aSingle.getCost7f()).add(aSingle.getCost8f())
                            .add(aSingle.getCost9f()).add(aSingle.getCost10f()).add(aSingle.getCost11f()).add(aSingle.getCost12f())
                            .add(aSingle.getCost1f()).add(aSingle.getCost2f()).add(aSingle.getCost3f());
                    BigDecimal sumMan = aSingle.getManhour4f().add(aSingle.getManhour5f()).add(aSingle.getManhour6f()).add(aSingle.getManhour7f()).add(aSingle.getManhour8f())
                            .add(aSingle.getManhour9f()).add(aSingle.getManhour10f()).add(aSingle.getManhour11f()).add(aSingle.getManhour12f())
                            .add(aSingle.getManhour1f()).add(aSingle.getManhour2f()).add(aSingle.getManhour3f());
                    aSingle.setTotalcostf(sumCost);
                    aSingle.setTotalmanhourf(sumMan);
                    aSingle.preInsert(tokenModel);
                    companyStatisticsMapper.insert(aSingle);
                }
            }

            if(!tempmonth.equals(""))
            {
                throw new LogicalException(tempmonth+"的费用已经审批，不允许再次变更金额。");
            }

        }
        //endregion 更新BP to
    }
}
