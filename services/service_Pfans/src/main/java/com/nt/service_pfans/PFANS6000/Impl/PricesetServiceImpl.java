package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.PricesetGroup;
import com.nt.dao_Pfans.PFANS6000.Vo.PricesetVo;
import com.nt.service_pfans.PFANS6000.PricesetService;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforDetailMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetGroupMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PricesetServiceImpl implements PricesetService {

    @Autowired
    private PricesetMapper pricesetMapper;

    @Autowired
    private PricesetGroupMapper pricesetGroupMapper;

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;
    @Autowired
    private ExpatriatesinforDetailMapper expatriatesinforDetailMapper;

    /**
     * 获取单价设定列表
     *
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public List<PricesetVo> gettlist(String pddate, String groupid) throws Exception {
        List<PricesetVo> rst = new ArrayList<PricesetVo>();
        PricesetVo a = new PricesetVo(new PricesetGroup(), new ArrayList<Priceset>());
        a.getMain().setPd_date(pddate);
        PricesetGroup pricesetGroup = new PricesetGroup();
        pricesetGroup.setPd_date(pddate);
        List<PricesetGroup> pricesetGroupList1 = new ArrayList<>();
        List<PricesetGroup> pricesetGroupList = new ArrayList<>();
        pricesetGroupList = pricesetGroupMapper.select(pricesetGroup);

        Priceset priceset = new Priceset();
        priceset.setGroup_id(groupid);

        String pricesetGroupid = "";
        String lastMounth ="";
        if(pricesetGroupList.size()>0)
        {
            pricesetGroupid = pricesetGroupList.get(0).getPricesetgroup_id();
            priceset.setPricesetgroup_id(pricesetGroupid);
            List<Priceset> pricesetList = pricesetMapper.select(priceset);
            if(pricesetList.size()>0)
            {
                for (Priceset pr : pricesetList) {
                    a.getDetail().add(pr);
                }
            }
            else
            {
                lastMounth = (DateUtil.format(DateUtil.offset(DateUtil.parse( pddate+ "-01"), DateField.MONTH,-1),"yyyy-MM"));
                pricesetGroup.setPd_date(lastMounth);
                pricesetGroupList1 = pricesetGroupMapper.select(pricesetGroup);
                if(pricesetGroupList1.size()>0)
                {
                    pricesetGroupid = pricesetGroupList1.get(0).getPricesetgroup_id();
                    priceset.setPricesetgroup_id(pricesetGroupid);
                    List<Priceset> pricesetList1 = pricesetMapper.select(priceset);
                    for (Priceset pr : pricesetList1) {
                        Expatriatesinfor e = new Expatriatesinfor();
                        e.setExpatriatesinfor_id(pr.getUser_id());
                        e = expatriatesinforMapper.selectByPrimaryKey(pr.getUser_id());
                        if(e.getGroup_id().equals(groupid) && e.getExits().equals("1") && e.getWhetherentry().equals("BP006001"))
                        {
                            pr.setPriceset_id(null);
                            a.getDetail().add(pr);
                        }
                    }
                }
            }
            a.getMain().setPricesetgroup_id(pricesetGroupList.get(0).getPricesetgroup_id());
        }
        else
        {
            lastMounth = (DateUtil.format(DateUtil.offset(DateUtil.parse( pddate+ "-01"), DateField.MONTH,-1),"yyyy-MM"));
            pricesetGroup.setPd_date(lastMounth);
            pricesetGroupList = pricesetGroupMapper.select(pricesetGroup);
            if(pricesetGroupList.size()>0)
            {
                pricesetGroupid = pricesetGroupList.get(0).getPricesetgroup_id();
                priceset.setPricesetgroup_id(pricesetGroupid);
                List<Priceset> pricesetList = pricesetMapper.select(priceset);
                for (Priceset pr : pricesetList) {
                    Expatriatesinfor e = new Expatriatesinfor();
                    e.setExpatriatesinfor_id(pr.getUser_id());
                    e = expatriatesinforMapper.selectByPrimaryKey(pr.getUser_id());
                    if(e.getGroup_id().equals(groupid) && e.getExits().equals("1") && e.getWhetherentry().equals("BP006001"))
                    {
                        pr.setPriceset_id(null);
                        a.getDetail().add(pr);
                    }
                }
            }
        }
        rst.add(a);
        return rst;
    }

    //add ccm 20201212
    //系统定时任务每月1号自动保存单价
    //@scheduled(cron = "0 0 4 1 * ?")
    public void savePriceset() throws Exception{
        //当月和上月的单价
        List<Priceset> pricesetListOLd = new ArrayList<>();
        //当月已经存在的单价
        List<Priceset> pricesetListNow = new ArrayList<>();

        LocalDate before = LocalDate.now().minusMonths(1);
        LocalDate beforetwo = LocalDate.now().minusMonths(1);
        beforetwo = before.minusMonths(1);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM");
        String lastmonthAntStr = formatters.format(beforetwo);
        String nowmonthAntStr = formatters.format(before);
        pricesetListOLd = pricesetMapper.selectBymonth(lastmonthAntStr,nowmonthAntStr);

        LocalDate now = LocalDate.now();
        String nowStr = formatters.format(now);
        PricesetGroup prg = new PricesetGroup();
        prg.setPd_date(nowStr);
        List<PricesetGroup> pricesetGroupList = new ArrayList<>();
        pricesetGroupList = pricesetGroupMapper.select(prg);
        if(pricesetGroupList.size()>0)
        {
            Priceset p = new Priceset();
            p.setPricesetgroup_id(pricesetGroupList.get(0).getPricesetgroup_id());
            pricesetListNow = pricesetMapper.select(p);

            prg.setPricesetgroup_id(pricesetGroupList.get(0).getPricesetgroup_id());
        }
        else
        {
            prg.preInsert();
            prg.setPricesetgroup_id(UUID.randomUUID().toString());
            pricesetGroupMapper.insert(prg);
        }

        //当前日期
        SimpleDateFormat st = new SimpleDateFormat("yyyyMMdd");
        int re = Integer.parseInt(st.format(new Date()));
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.selectAll();
        if (expatriatesinforList.size() > 0) {
            for (Expatriatesinfor ex : expatriatesinforList) {
                List<Priceset> tempPrice =new ArrayList<>();
                if(pricesetListNow.size()>0)
                {
                    tempPrice = pricesetListNow.stream().filter(item -> (item.getGroup_id().equals(ex.getGroup_id()) && item.getUser_id().equals(ex.getExpatriatesinfor_id()))).collect(Collectors.toList());
                }
                if(tempPrice.size() == 0)
                {
                    //已经退场，保留退场月的单价
                    if (ex.getExitime() != null) {
                        if (re < Integer.parseInt(st.format(ex.getExitime()))) {
                            tempPrice = pricesetListOLd.stream().filter(item -> (item.getGroup_id().equals(ex.getGroup_id()) && item.getUser_id().equals(ex.getExpatriatesinfor_id()))).collect(Collectors.toList());
                        }
                    }
                    else
                    {
                        tempPrice = pricesetListOLd.stream().filter(item -> (item.getGroup_id().equals(ex.getGroup_id()) && item.getUser_id().equals(ex.getExpatriatesinfor_id()))).collect(Collectors.toList());
                    }

                    //历史单价中存在
                    if(tempPrice.size()>0)
                    {
                        tempPrice.get(0).setPricesetgroup_id(prg.getPricesetgroup_id());
                        tempPrice.get(0).setPriceset_id(UUID.randomUUID().toString());
                        tempPrice.get(0).preInsert();
                        tempPrice.get(0).setGroup_id(ex.getGroup_id());
                        tempPrice.get(0).setUser_id(ex.getExpatriatesinfor_id());
                        pricesetMapper.insert(tempPrice.get(0));
                    }
                    else
                    {
                        if(ex.getExits().equals("1") && ex.getWhetherentry().equals("BP006001"))
                        {
                            TokenModel tokenModel = new TokenModel();
                            String thisDate = DateUtil.format(new Date(), "yyyy-MM-dd");
                            Priceset p = new Priceset();
                            p.preInsert(tokenModel);
                            p.setPriceset_id(UUID.randomUUID().toString());
                            p.setUser_id(ex.getExpatriatesinfor_id());
                            p.setGroup_id(ex.getGroup_id());
                            p.setGraduation(ex.getGraduation_year());
                            p.setCompany(ex.getSuppliername());
                            p.setAssesstime(thisDate);
                            p.setUsername(ex.getExpname());
                            p.setPricesetgroup_id(prg.getPricesetgroup_id());
                            p.setStatus("0");
                            pricesetMapper.insert(p);
                        }
                    }
                }
            }
        }
    }
    //add ccm 20201212

    @Override
    public List<Priceset> getPricesetList(Priceset priceset) throws Exception {
        List<Priceset> pricesetList = pricesetMapper.select(priceset);
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.selectAll();
        pricesetList.forEach(item -> {
            item.setUsername(expatriatesinforList.stream().filter(subItem -> subItem.getExpatriatesinfor_id().equals(item.getUser_id())).collect(Collectors.toList()).get(0).getExpname());
        });
        return pricesetList;
    }


    @Override
    public Priceset pricesetgenerate(String pricesetid) throws Exception {
        return null;
    }

    /**
     * 单价设定修改
     *
     * @param pricesetVo
     * @param tokenModel
     * @throws Exception
     */
    @Override
    public void updatepriceset(PricesetVo pricesetVo, TokenModel tokenModel) throws Exception {

        if (StrUtil.isNotBlank(pricesetVo.getMain().getPricesetgroup_id())) {
            pricesetVo.getMain().preUpdate(tokenModel);
            pricesetGroupMapper.updateByPrimaryKeySelective(pricesetVo.getMain());
        } else {
            pricesetVo.getMain().preInsert(tokenModel);
            pricesetVo.getMain().setPricesetgroup_id(UUID.randomUUID().toString());
            pricesetGroupMapper.insert(pricesetVo.getMain());
        }

        for (Priceset priceset : pricesetVo.getDetail()) {
            if (StrUtil.isNotBlank(priceset.getPriceset_id())) {
                priceset.preUpdate(tokenModel);
                pricesetMapper.updateByPrimaryKeySelective(priceset);
            } else {
                priceset.preInsert(tokenModel);
                priceset.setPriceset_id(UUID.randomUUID().toString());
                priceset.setPricesetgroup_id(pricesetVo.getMain().getPricesetgroup_id());
                pricesetMapper.insert(priceset);
            }
        }
    }
}
