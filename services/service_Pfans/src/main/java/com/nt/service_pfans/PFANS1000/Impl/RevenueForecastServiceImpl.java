package com.nt.service_pfans.PFANS1000.Impl;
import cn.hutool.core.codec.Base64;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.dao_Pfans.PFANS1000.RevenueForecast;
import com.nt.dao_Pfans.PFANS1000.ThemeInfor;
import com.nt.dao_Pfans.PFANS1000.Vo.RevenueForecastVo;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.RevenueForecastService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentAccountMapper;
import com.nt.service_pfans.PFANS1000.mapper.RevenueForecastMapper;
import com.nt.service_pfans.PFANS1000.mapper.ThemeInforMapper;
import com.nt.utils.PageUtil;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TableDataInfo;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Theme别收入见通(RevenueForecast)表服务实现类
 *
 * @author makejava
 * @since 2021-11-18 14:58:51
 */
@Service("revenueForecastService")
@Transactional(rollbackFor = Exception.class)
public class RevenueForecastServiceImpl implements RevenueForecastService {

    @Autowired
    private RevenueForecastMapper revenueForecastMapper;

    @Autowired
    private ThemeInforMapper themeInforMapper;

    @Autowired
    private DepartmentAccountMapper departmentAccountMapper;

    @Autowired
    private DepartmentAccountService departmentAccountService;

    @Autowired
    private OrgTreeService orgTreeService;

    /**
     * 保存信息
     *
     * @param revenueForecastVo
     * @param tokenModel        令牌模型
     */
    @Override
    public void saveInfo(RevenueForecastVo revenueForecastVo, TokenModel tokenModel) throws Exception{
        RevenueForecast revenueForecastInfo = revenueForecastVo.getRevenueForecast();
        //获取参数
        Date saveDate = revenueForecastInfo.getSaveDate();
        LocalDate localDate = saveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //年份
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int annual = year;
        if (month < 4) {
            annual = year - 1;
        }
        //部门ID
        String deptId = revenueForecastInfo.getDeptId();

        List<RevenueForecast> revenueForecastList = revenueForecastVo.getRevenueForecastList();
        if(revenueForecastList.size() > 0){
            for (RevenueForecast revenueForecast : revenueForecastList){
                //region scc del 现无页面创建theme from
                //有新添加的theme数据 进行创建
//                if(StringUtils.isEmpty(revenueForecast.getThemeinforId()) || revenueForecast.getThemeinforId().equals(revenueForecast.getThemeName())){
//                    ThemeInfor themeInfor = new ThemeInfor();
//                    themeInfor.preInsert(tokenModel);
//                    themeInfor.setThemeinfor_id(UUID.randomUUID().toString());
//                    themeInfor.setThemename(revenueForecast.getThemeName());
//                    themeInfor.setToolsorgs(revenueForecast.getCustomerName());
//                    themeInfor.setYear(String.valueOf(year));
//                    themeInforMapper.insert(themeInfor);
//
//                    revenueForecast.setThemeinforId(themeInfor.getThemeinfor_id());
                //endregion scc del 现无页面创建theme to
//                }

                if(StringUtils.isEmpty(revenueForecast.getId())){
                    revenueForecast.preInsert(tokenModel);
                    revenueForecast.setId(UUID.randomUUID().toString());
                    revenueForecast.setSaveDate(saveDate);
                    revenueForecast.setDeptId(deptId);
                    revenueForecast.setAnnual(String.valueOf(annual));
                }else{
                    revenueForecast.setAnnual(String.valueOf(annual));
                    revenueForecast.preUpdate(tokenModel);
                }

                //region scc del 影响更新速度，暂时注调 from
//                //theme别合同收支分析表更新
//                //先检索 判断是否有相关数据
//                DepartmentAccount departmentAccount = new DepartmentAccount();
//                departmentAccount.setTheme_id(revenueForecast.getThemeinforId());
//                departmentAccount.setDepartment(deptId);
//                departmentAccount.setYears(String.valueOf(year));
//
//                List<DepartmentAccount> departmentAccountlist =  departmentAccountMapper.select(departmentAccount);
//                //对已有数据实际值进行更新
//                if(departmentAccountlist.size() > 0){
//                    for (DepartmentAccount deaccount : departmentAccountlist){
//                        deaccount.setMoneyplan1(new BigDecimal(revenueForecast.getJanuaryForecast()));
//                        deaccount.setMoneyplan2(new BigDecimal(revenueForecast.getFebruaryForecast()));
//                        deaccount.setMoneyplan3(new BigDecimal(revenueForecast.getMarchForecast()));
//                        deaccount.setMoneyplan4(new BigDecimal(revenueForecast.getAprilForecast()));
//                        deaccount.setMoneyplan5(new BigDecimal(revenueForecast.getMayForecast()));
//                        deaccount.setMoneyplan6(new BigDecimal(revenueForecast.getJuneForecast()));
//                        deaccount.setMoneyplan7(new BigDecimal(revenueForecast.getJulyForecast()));
//                        deaccount.setMoneyplan8(new BigDecimal(revenueForecast.getAugustForecast()));
//                        deaccount.setMoneyplan9(new BigDecimal(revenueForecast.getSeptemberForecast()));
//                        deaccount.setMoneyplan10(new BigDecimal(revenueForecast.getOctoberForecast()));
//                        deaccount.setMoneyplan11(new BigDecimal(revenueForecast.getNovemberForecast()));
//                        deaccount.setMoneyplan12(new BigDecimal(revenueForecast.getDecemberForecast()));
//                        departmentAccountMapper.updateByPrimaryKeySelective(deaccount);
//                    }
//                }else{
//                    //调用DepartmentAccountService的insert()
//                    departmentAccountService.insert();
//                }
                //endregion scc del 影响更新速度，暂时注调 to
            }

            revenueForecastMapper.insertOrUpdateBatch(revenueForecastList);
        }
    }

    @Override
    public List<RevenueForecast> selectInfo(RevenueForecast revenueForecast) throws ParseException,Exception {
        //id--部门键值对
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        HashMap<String,String> companyid = new HashMap<>();
        for(DepartmentVo vo : allDepartment){
            companyid.put(vo.getDepartmentId(),vo.getDepartmentname());
        }
        List<RevenueForecast> listForReturn = new ArrayList<RevenueForecast>();
        //获取参数
        Date saveDate = revenueForecast.getSaveDate();
        //部门ID
        String deptId = revenueForecast.getDeptId();
        LocalDate localDate = saveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //根据月份判断获取年份
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        if(month < 4){
            year = year - 1;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        revenueForecast.setSaveDate(sdf.parse(sdf.format(revenueForecast.getSaveDate())));
        //判断是否为第一次填写
        List<RevenueForecast> revenueForecastlist = revenueForecastMapper.selectOldRevenueForecastList(deptId,year,saveDate,revenueForecast.getThemeName());
        //ccm 20211231 客户名有可能是社内组织，数据库中是ID，需要转换 fr
        if (revenueForecastlist.size() > 0) {
            OrgTree org = orgTreeService.get(new OrgTree());
            for (RevenueForecast value : revenueForecastlist) {
                OrgTree orginfo = orgTreeService.getOrgInfo(org, value.getCustomerName());

                if(orginfo!=null )
                {
                    if(orginfo.getCompanyname()!=null && !orginfo.getCompanyname().equals(""))
                    {
                        value.setCustomerName(orginfo.getCompanyname());
                    }
                }
            }
        }
        //ccm 20211231 客户名有可能是社内组织，数据库中是ID，需要转换 to
        //region scc del 转为定时 from
//        if(revenueForecastlist.size() == 0){
//            //从theme表里获取
//            listForReturn = revenueForecastMapper.selectRevenueForecastListFirst(deptId,year,saveDate);
//        }else{
//            //从revenue表里获取
//            //判断查询月份是否为之前月份
//            LocalDate localNow = LocalDate.now();
//            int monthNow = localNow.getMonthValue();
//            int yearNow = localNow.getYear();
//            //获取之前的数据
//            if(year <= yearNow  && month <= monthNow){
//                listForReturn = revenueForecastlist;
//            }else{
//                //获取实际数据
//                listForReturn = revenueForecastMapper.selectRevenueForecastList(deptId,year,saveDate);
//            }
//        }
//        listForReturn.forEach(item -> {
//            if(companyid.get(item.getCustomerName()) != null){
//                item.setCustomerName(companyid.get(item.getCustomerName().trim()));//客户名
//            }else{
//                item.setCustomerName(item.getCustomerName());//客户名
//            }
//        });
        //endregion scc del 转为定时 from
        return revenueForecastlist;
    }


    @Override
    public TableDataInfo getThemeOutDepth(RevenueForecast revenueForecast, int currentPage, int pageSize){
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        List<RevenueForecast> listForReturn = new ArrayList<RevenueForecast>();
        //获取参数
        Date saveDate = revenueForecast.getSaveDate();
        //部门ID
        String deptId = revenueForecast.getDeptId();
        LocalDate localDate = saveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //根据月份判断获取年份
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        if(month < 4){
            year = year - 1;
        }

        //从theme表里获取
        listForReturn = revenueForecastMapper.getThemeOutDepth(deptId,year,saveDate);
        Page<RevenueForecast> pageFromList = PageUtil.createPageFromList(listForReturn, pageable);
        TableDataInfo taInfo = new TableDataInfo();
        taInfo.setTotal(pageFromList.getTotalElements() > listForReturn.size() ? listForReturn.size() : pageFromList.getTotalElements());
        taInfo.setResultList(pageFromList.getContent());
        return taInfo;
    }

    @Override
    public void saveAuto() throws Exception{
        TokenModel tokenModel = new TokenModel();
        //id--部门键值对
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        HashMap<String,String> company = new HashMap<>();
        List<String> companyid = new ArrayList<>();//所有有效部门
        for(DepartmentVo vo : allDepartment){
            company.put(vo.getDepartmentId(),vo.getDepartmentname());
            companyid.add(vo.getDepartmentId());
        }
        List<RevenueForecast> listForReturn = new ArrayList<RevenueForecast>();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;//当前月
        int year = cal.get(Calendar.YEAR);//当前年
        if (month < 4) {
            year = year - 1;//当前年度
        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date parse = sdf.parse(sdf.format(date));
        for (String item : companyid) {
            listForReturn = revenueForecastMapper.selectOldRevenueForecastList(item, year, date,null);
            if (listForReturn != null && listForReturn.size() > 0) {
                continue;
            }else{
                listForReturn = revenueForecastMapper.selectRevenueForecastListFirst(item,year,date);
                for (RevenueForecast items : listForReturn) {
                    items.setId(UUID.randomUUID().toString());
                    items.setDeptId(item);
                    items.setSaveDate(date);
                    items.setAnnual(String.valueOf(year));
                    items.preInsert();
                    items.preUpdate(tokenModel);
                    if (company.get(items.getCustomerName()) != null) {
                        items.setCustomerName(company.get(items.getCustomerName().trim()));//客户名
                    } else {
                        items.setCustomerName(items.getCustomerName());//客户名
                    }
                }
                if (listForReturn.size() > 0 && listForReturn != null) {
                    revenueForecastMapper.insertOrUpdateBatch(listForReturn);
                }
            }
        }

    }
}
