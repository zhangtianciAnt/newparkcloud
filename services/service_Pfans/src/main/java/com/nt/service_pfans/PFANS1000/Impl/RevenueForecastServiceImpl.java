package com.nt.service_pfans.PFANS1000.Impl;
import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.dao_Pfans.PFANS1000.RevenueForecast;
import com.nt.dao_Pfans.PFANS1000.ThemeInfor;
import com.nt.dao_Pfans.PFANS1000.Vo.RevenueForecastVo;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.RevenueForecastService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentAccountMapper;
import com.nt.service_pfans.PFANS1000.mapper.RevenueForecastMapper;
import com.nt.service_pfans.PFANS1000.mapper.ThemeInforMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    /**
     * 保存信息
     *
     * @param revenueForecastVo
     * @param tokenModel        令牌模型
     */
    @Override
    public void saveInfo(RevenueForecastVo revenueForecastVo, TokenModel tokenModel) throws Exception{
        //有新添加的theme数据 进行创建
        List<ThemeInfor> themeInforlist = revenueForecastVo.getThemeInforList();
        if(themeInforlist.size() > 0){
            for(ThemeInfor themeInfor : themeInforlist){
                themeInfor.preInsert(tokenModel);
                themeInfor.setThemeinfor_id(UUID.randomUUID().toString());
            }
            themeInforMapper.insertListAllCols(themeInforlist);
        }

        List<RevenueForecast> revenueForecastList = revenueForecastVo.getRevenueForecastList();
        if(revenueForecastList.size() > 0){
            for (RevenueForecast revenueForecast : revenueForecastList){
                if(StringUtils.isEmpty(revenueForecast.getId())){
                    revenueForecast.preInsert(tokenModel);
                    revenueForecast.setId(UUID.randomUUID().toString());
                }else{
                    revenueForecast.preUpdate(tokenModel);
                }

                //theme别合同收支分析表更新
                //先检索 判断是否有相关数据
                //获取参数
                Date saveDate = revenueForecast.getSaveDate();
                LocalDate localDate = saveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                //年份
                int year = localDate.getYear();

                DepartmentAccount departmentAccount = new DepartmentAccount();
                departmentAccount.setTheme_id(revenueForecast.getThemeinforId());
                departmentAccount.setDepartment(revenueForecast.getDeptId());
                departmentAccount.setYears(String.valueOf(year));

                List<DepartmentAccount> departmentAccountlist =  departmentAccountMapper.select(departmentAccount);
                //对已有数据实际值进行更新
                if(departmentAccountlist.size() > 0){
                    for (DepartmentAccount deaccount : departmentAccountlist){
                        deaccount.setMoneyplan1(new BigDecimal(revenueForecast.getJanuaryForecast()));
                        deaccount.setMoneyplan2(new BigDecimal(revenueForecast.getFebruaryForecast()));
                        deaccount.setMoneyplan3(new BigDecimal(revenueForecast.getMarchForecast()));
                        deaccount.setMoneyplan4(new BigDecimal(revenueForecast.getAprilForecast()));
                        deaccount.setMoneyplan5(new BigDecimal(revenueForecast.getMayForecast()));
                        deaccount.setMoneyplan6(new BigDecimal(revenueForecast.getJuneForecast()));
                        deaccount.setMoneyplan7(new BigDecimal(revenueForecast.getJulyForecast()));
                        deaccount.setMoneyplan8(new BigDecimal(revenueForecast.getAugustForecast()));
                        deaccount.setMoneyplan9(new BigDecimal(revenueForecast.getSeptemberForecast()));
                        deaccount.setMoneyplan10(new BigDecimal(revenueForecast.getOctoberForecast()));
                        deaccount.setMoneyplan11(new BigDecimal(revenueForecast.getNovemberForecast()));
                        deaccount.setMoneyplan12(new BigDecimal(revenueForecast.getDecemberForecast()));
                        departmentAccountMapper.updateByPrimaryKeySelective(deaccount);
                    }
                }else{
                    //调用DepartmentAccountService的insert()
                    departmentAccountService.insert();
                }
            }

            revenueForecastMapper.insertOrUpdateBatch(revenueForecastList);
        }
    }

    @Override
    public List<RevenueForecast> selectInfo(RevenueForecast revenueForecast){
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

        //判断是否为第一次填写
        List<RevenueForecast> RevenueForecastlist = revenueForecastMapper.selectAll();
        if(RevenueForecastlist.size() == 0){
            //从theme表里获取
            listForReturn = revenueForecastMapper.selectRevenueForecastListFirst(deptId,year,saveDate);
        }else{
            //从revenue表里获取
            //判断查询月份是否为之前月份
            LocalDate localNow = LocalDate.now();
            int monthNow = localNow.getMonthValue();
            //获取之前的数据
            if(localDate.isBefore(localNow) && month < monthNow){
                listForReturn = revenueForecastMapper.select(revenueForecast);
            }else{
                //获取实际数据
                listForReturn = revenueForecastMapper.selectRevenueForecastList(deptId,year,saveDate);
            }
        }

        return listForReturn;
    }
}
