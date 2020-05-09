package com.nt.service_pfans.PFANS1000.Impl;

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
    public List<ThemePlanDetailVo> detilList(ThemePlanDetail themePlanDetail) throws Exception {
        List<ThemePlanDetailVo> colist = new ArrayList<ThemePlanDetailVo>();
        List<ThemePlanDetail> Detaillist = new ArrayList<ThemePlanDetail>();
        Detaillist = themePlanDetailMapper.select(themePlanDetail);
        for(ThemePlanDetail tpd : Detaillist){
            ThemePlanDetailVo Vo = new ThemePlanDetailVo();
            ThemePlanDetail de = new ThemePlanDetail();
            de.setPthemeplandetail_id(tpd.getThemeplandetail_id());
            List<ThemePlanDetail> detailp = new ArrayList<ThemePlanDetail>();
            detailp = themePlanDetailMapper.select(de);
            Vo.setThemeplandetail(tpd);
            Vo.setChildren(detailp);
            colist.add(Vo);
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
}
