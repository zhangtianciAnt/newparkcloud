package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS1000.Routingdetail;
import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemePlanDetailVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemePlanVo;
import com.nt.service_pfans.PFANS1000.ThemePlanService;
import com.nt.service_pfans.PFANS1000.mapper.ThemePlanDetailMapper;
import com.nt.service_pfans.PFANS1000.mapper.ThemePlanMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ThemePlanServiceImpl implements ThemePlanService {

    @Autowired
    private ThemePlanMapper themePlanMapper;

    @Autowired
    private ThemePlanDetailMapper themePlanDetailMapper;

    @Override
    public List<ThemePlan> getList(ThemePlan themePlan) throws Exception {
        List<ThemePlan> colist = new ArrayList<ThemePlan>();
        colist = themePlanMapper.select(themePlan);
        Comparator<ThemePlan> byYear = Comparator.comparing(ThemePlan::getYear).reversed();
        Comparator<ThemePlan> byCreateon = Comparator.comparing(ThemePlan::getCreateon).reversed();
        //按年度，创建时间倒序排序
        colist.sort(byYear.thenComparing(byCreateon));
        return colist;
    }

    @Override
    public List<ThemePlanDetail> get(ThemePlan themePlan) throws Exception {
        List<ThemePlanDetail> colist = new ArrayList<ThemePlanDetail>();
        colist = themePlanDetailMapper.getDetailList(themePlan.getThemeplan_id());
        if (colist.size() > 0) {
            colist = colist.stream().sorted(Comparator.comparing(ThemePlanDetail::getRowindex)).collect(Collectors.toList());
        }
        return colist;
    }

    @Override
    public void insert(ThemePlanVo themePlan, TokenModel tokenModel) throws LogicalException {
        List<ThemePlan> colist = themePlan.getThemePlans();
        for (ThemePlan item : colist
        ) {
            ThemePlan ct = new ThemePlan();
            ct.setCenter_id(item.getCenter_id());
            ct.setGroup_id(item.getGroup_id());
            ct.setYear(item.getYear());
            ct.setType(item.getType());
            //该group该年度theme是否已创建
            colist = themePlanMapper.select(ct);
            if (colist.size() > 0) {
                throw new LogicalException("本部门该年度theme已经创建，请到列表页中查找编辑。");
            }
            ct.setThemeplan_id(UUID.randomUUID().toString());
            ct.setFinancialprocessingflg("0");
            ct.preInsert(tokenModel);
            themePlanMapper.insert(ct);

            //创建新的theme detail
            List<ThemePlanDetail> colistDetail = themePlan.getThemePlanDetails();
            for (ThemePlanDetail item2 : colistDetail) {
                ThemePlanDetail ctDetail = new ThemePlanDetail();
                ctDetail = item2;
                ctDetail.setThemeplan_id(ct.getThemeplan_id());
                ctDetail.setCenter_id(ct.getCenter_id());
                ctDetail.setGroup_id(ct.getGroup_id());
                ctDetail.setYear(ct.getYear());
                ctDetail.preInsert(tokenModel);
                ctDetail.setThemeplandetail_id(UUID.randomUUID().toString());
                themePlanDetailMapper.insert(ctDetail);
            }
        }
    }

    @Override
    public void update(ThemePlanVo themePlan, TokenModel tokenModel) throws LogicalException {
        List<ThemePlan> colist = themePlan.getThemePlans();
        for (ThemePlan item : colist
        ) {
            if (item.getThemeplan_id() != null) {
                //更新theme detail
                List<ThemePlanDetail> colistDetail = themePlan.getThemePlanDetails();
                for (ThemePlanDetail item2 : colistDetail) {
                    ThemePlanDetail ctDetail = new ThemePlanDetail();
                    ctDetail = item2;
                    if (ctDetail.getThemeplandetail_id() == null || ctDetail.getThemeplandetail_id() == "") {
                        ctDetail.setThemeplan_id(item.getThemeplan_id());
                        ctDetail.setCenter_id(item.getCenter_id());
                        ctDetail.setGroup_id(item.getGroup_id());
                        ctDetail.setYear(item.getYear());
                        ctDetail.preInsert(tokenModel);
                        ctDetail.setThemeplandetail_id(UUID.randomUUID().toString());
                        themePlanDetailMapper.insert(ctDetail);
                    } else {
                        ctDetail.preUpdate(tokenModel);
                        themePlanDetailMapper.updateByPrimaryKey(ctDetail);
                    }
                }
            } else {
                throw new LogicalException("data error");
            }
        }
    }

    @Override
    public List<ThemePlanDetailVo> detilList(ThemePlanDetail themePlanDetail) throws Exception {
        List<ThemePlanDetailVo> colist = new ArrayList<ThemePlanDetailVo>();
        List<ThemePlanDetail> Detaillist = new ArrayList<ThemePlanDetail>();
        Detaillist = themePlanDetailMapper.select(themePlanDetail);
        for(ThemePlanDetail tpd : Detaillist){
            ThemePlanDetailVo Vo = new ThemePlanDetailVo();

            //region 赋值
            Vo.setThemeplandetail_id(tpd.getThemeplandetail_id());

            Vo.setThemeplan_id(tpd.getThemeplan_id());

            Vo.setPthemeplandetail_id(tpd.getPthemeplandetail_id());

            Vo.setCenter_id(tpd.getCenter_id());

            Vo.setGroup_id(tpd.getGroup_id());

            Vo.setYear(tpd.getYear());

            Vo.setMonth(tpd.getMonth());

            Vo.setTheme_id(tpd.getTheme_id());

            Vo.setThemename(tpd.getThemename());

            Vo.setRemarks(tpd.getRemarks());

            Vo.setBranch(tpd.getBranch());

            Vo.setKind(tpd.getKind());

            Vo.setContracttype(tpd.getContracttype());

            Vo.setCurrencytype(tpd.getCurrencytype());

            Vo.setAssignor(tpd.getAssignor());

            Vo.setPersonnel191(tpd.getPersonnel191());

            Vo.setAmount191(tpd.getAmount191());

            Vo.setPersonnel192(tpd.getPersonnel192());

            Vo.setAmount192(tpd.getAmount192());

            Vo.setPersonnel193(tpd.getPersonnel193());

            Vo.setAmount193(tpd.getAmount193());

            Vo.setPersonnel4(tpd.getPersonnel4());

            Vo.setAmount4(tpd.getAmount4());

            Vo.setPersonnel5(tpd.getPersonnel5());

            Vo.setAmount5(tpd.getAmount5());

            Vo.setPersonnel6(tpd.getPersonnel6());

            Vo.setAmount6(tpd.getAmount6());

            Vo.setPersonnel7(tpd.getPersonnel7());

            Vo.setAmount7(tpd.getAmount7());

            Vo.setPersonnel8(tpd.getPersonnel8());

            Vo.setAmount8(tpd.getAmount8());

            Vo.setPersonnel9(tpd.getPersonnel9());

            Vo.setAmount9(tpd.getAmount9());

            Vo.setPersonnel10(tpd.getPersonnel10());

            Vo.setAmount10(tpd.getAmount10());

            Vo.setPersonnel11(tpd.getPersonnel11());

            Vo.setAmount11(tpd.getAmount11());

            Vo.setPersonnel12(tpd.getPersonnel12());

            Vo.setAmount12(tpd.getAmount12());

            Vo.setPersonnel1(tpd.getPersonnel1());

            Vo.setAmount1(tpd.getAmount1());

            Vo.setPersonnel2(tpd.getPersonnel2());

            Vo.setAmount2(tpd.getAmount2());

            Vo.setPersonnel3(tpd.getPersonnel3());

            Vo.setAmount3(tpd.getAmount3());

            Vo.setType(tpd.getType());

            Vo.setRowindex(tpd.getRowindex());

            Vo.setRowid(tpd.getRowid());

            Vo.setStatus(tpd.getStatus());
            //endregion

            ThemePlanDetail de = new ThemePlanDetail();
            de.setPthemeplandetail_id(tpd.getThemeplandetail_id());
            List<ThemePlanDetail> detailp = themePlanDetailMapper.select(de);
            detailp = detailp.stream().sorted(Comparator.comparing(ThemePlanDetail::getRowindex)).collect(Collectors.toList());
            Vo.setChildren(detailp);
            colist.add(Vo);
        }
        colist = colist.stream().sorted(Comparator.comparing(ThemePlanDetailVo::getRowindex)).collect(Collectors.toList());
        return colist;
    }

    @Override
    public void inserttheme(List<ThemePlanDetailVo> themePlanDetailVo, TokenModel tokenModel) throws LogicalException {
        int plancount = 0;
        if(themePlanDetailVo.size() > 0){
            String themePlanid = UUID.randomUUID().toString();
            if(!StringUtils.isNullOrEmpty(themePlanDetailVo.get(0).getThemeplan_id())){
                themePlanid = themePlanDetailVo.get(0).getThemeplan_id();
            }
            //region theme计划
            ThemePlan ct = new ThemePlan();
            ct.setCenter_id(themePlanDetailVo.get(0).getCenter_id());
            ct.setGroup_id(themePlanDetailVo.get(0).getGroup_id());
            ct.setYear(themePlanDetailVo.get(0).getYear());
            ct.setType(themePlanDetailVo.get(0).getPlantype());
            if(StringUtils.isNullOrEmpty(themePlanDetailVo.get(0).getThemeplan_id())){
                //该group该年度theme是否已创建
                List<ThemePlan> colist = themePlanMapper.select(ct);
                if (colist.size() > 0) {
                    throw new LogicalException("本部门该年度theme已经创建，请到列表页中查找编辑。");
                }
            }
            int rowindex = 0;
            for (ThemePlanDetailVo vo : themePlanDetailVo) {
                rowindex = rowindex + 1;
                plancount = plancount + 1;
                String themeplandetailid = UUID.randomUUID().toString();
                if(!StringUtils.isNullOrEmpty(vo.getThemeplandetail_id())){
                    themeplandetailid = vo.getThemeplandetail_id();
                }
                ThemePlanDetail ctDetail = new ThemePlanDetail();

                //region ThemePlanDetail赋值

                ctDetail.setThemeplandetail_id(vo.getThemeplandetail_id());

                ctDetail.setThemeplan_id(vo.getThemeplan_id());

                ctDetail.setPthemeplandetail_id(vo.getPthemeplandetail_id());

                ctDetail.setCenter_id(vo.getCenter_id());

                ctDetail.setGroup_id(vo.getGroup_id());

                ctDetail.setYear(vo.getYear());

                ctDetail.setMonth(vo.getMonth());

                ctDetail.setTheme_id(vo.getTheme_id());

                ctDetail.setThemename(vo.getThemename());

                ctDetail.setRemarks(vo.getRemarks());

                ctDetail.setBranch(vo.getBranch());

                ctDetail.setKind(vo.getKind());

                ctDetail.setContracttype(vo.getContracttype());

                ctDetail.setCurrencytype(vo.getCurrencytype());

                ctDetail.setAssignor(vo.getAssignor());

                ctDetail.setPersonnel191(vo.getPersonnel191());

                ctDetail.setAmount191(vo.getAmount191());

                ctDetail.setPersonnel192(vo.getPersonnel192());

                ctDetail.setAmount192(vo.getAmount192());

                ctDetail.setPersonnel193(vo.getPersonnel193());

                ctDetail.setAmount193(vo.getAmount193());

                ctDetail.setPersonnel4(vo.getPersonnel4());

                ctDetail.setAmount4(vo.getAmount4());

                ctDetail.setPersonnel5(vo.getPersonnel5());

                ctDetail.setAmount5(vo.getAmount5());

                ctDetail.setPersonnel6(vo.getPersonnel6());

                ctDetail.setAmount6(vo.getAmount6());

                ctDetail.setPersonnel7(vo.getPersonnel7());

                ctDetail.setAmount7(vo.getAmount7());

                ctDetail.setPersonnel8(vo.getPersonnel8());

                ctDetail.setAmount8(vo.getAmount8());

                ctDetail.setPersonnel9(vo.getPersonnel9());

                ctDetail.setAmount9(vo.getAmount9());

                ctDetail.setPersonnel10(vo.getPersonnel10());

                ctDetail.setAmount10(vo.getAmount10());

                ctDetail.setPersonnel11(vo.getPersonnel11());

                ctDetail.setAmount11(vo.getAmount11());

                ctDetail.setPersonnel12(vo.getPersonnel12());

                ctDetail.setAmount12(vo.getAmount12());

                ctDetail.setPersonnel1(vo.getPersonnel1());

                ctDetail.setAmount1(vo.getAmount1());

                ctDetail.setPersonnel2(vo.getPersonnel2());

                ctDetail.setAmount2(vo.getAmount2());

                ctDetail.setPersonnel3(vo.getPersonnel3());

                ctDetail.setAmount3(vo.getAmount3());

                ctDetail.setType(vo.getType());

                ctDetail.setRowindex(vo.getRowindex());

                ctDetail.setRowid(vo.getRowid());
                //endregion

                ctDetail.setRowindex(String.valueOf(rowindex));
                if(!StringUtils.isNullOrEmpty(ctDetail.getThemeplandetail_id())){
                    ctDetail.preUpdate(tokenModel);
                    ctDetail.setStatus(vo.getStatus());
                    themePlanDetailMapper.updateByPrimaryKeySelective(ctDetail);
                }
                else{
                    ctDetail.preInsert(tokenModel);
                    ctDetail.setThemeplandetail_id(themeplandetailid);
                    ctDetail.setThemeplan_id(themePlanid);
                    themePlanDetailMapper.insert(ctDetail);
                }
                if(vo.getChildren().size() > 0){
                    int derowindex = 0;
                    for (ThemePlanDetail tpd : vo.getChildren()) {
                        derowindex = derowindex + 1;
                        plancount = plancount + 1;
                        ThemePlanDetail themePlanDetail = tpd;
                        themePlanDetail.setRowindex(String.valueOf(derowindex));
                        themePlanDetail.setCenter_id(ctDetail.getCenter_id());
                        themePlanDetail.setGroup_id(ctDetail.getGroup_id());
                        themePlanDetail.setYear(ctDetail.getYear());
                        if(!StringUtils.isNullOrEmpty(themePlanDetail.getThemeplandetail_id())){
                            themePlanDetail.preUpdate(tokenModel);
                            themePlanDetail.setStatus(tpd.getStatus());
                            themePlanDetailMapper.updateByPrimaryKeySelective(themePlanDetail);
                        }
                        else{
                            themePlanDetail.preInsert(tokenModel);
                            themePlanDetail.setThemeplandetail_id(UUID.randomUUID().toString());
                            themePlanDetail.setPthemeplandetail_id(themeplandetailid);
                            themePlanDetail.setThemeplan_id(themePlanid);
                            themePlanDetailMapper.insert(themePlanDetail);
                        }
                    }
                }
            }
            ct.setPlancount(String.valueOf(plancount));
            if(StringUtils.isNullOrEmpty(themePlanDetailVo.get(0).getThemeplan_id())){
                ct.setThemeplan_id(themePlanid);
                ct.setFinancialprocessingflg("0");
                ct.preInsert(tokenModel);
                themePlanMapper.insert(ct);
            }
            else{
                ct.preUpdate(tokenModel);
                ct.setThemeplan_id(themePlanDetailVo.get(0).getThemeplan_id());
                ct.setStatus(themePlanDetailVo.get(0).getStatus());
                themePlanMapper.updateByPrimaryKeySelective(ct);
            }
            // endregion
        }
    }
}
