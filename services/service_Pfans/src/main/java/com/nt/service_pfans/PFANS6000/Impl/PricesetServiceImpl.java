package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.ExpatriatesinforDetail;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
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
        //在职，当月有单价
        List<Priceset> pricesetList = pricesetMapper.selectThismonth(pddate,groupid);
        //查询当月没有单价的人员 selectBpeople返回外驻人员登记表主键
        List<String> peopleAnt = pricesetMapper.selectBpeople(pddate,groupid);

        String lastMounth = "";
        lastMounth = (DateUtil.format(DateUtil.offset(DateUtil.parse(pricesetGroupMapper.selectByPrimaryKey
                (pricesetList.get(0).getPricesetgroup_id()).getPd_date() + "-01"), DateField.MONTH,-1),"yyyy-MM"));
        PricesetGroup pricesetGroup = new PricesetGroup();
        pricesetGroup.setPd_date(lastMounth);
        String PricesetGroup_id = pricesetGroupMapper.select(pricesetGroup).get(0).getPricesetgroup_id();

        for(String ms:peopleAnt){
            Priceset priceset = new Priceset();
            priceset.setUser_id(ms);
            priceset.setPricesetgroup_id(PricesetGroup_id);
            List<Priceset> pricesetAntList = pricesetMapper.select(priceset);
            Expatriatesinfor expatriatesinfor = expatriatesinforMapper.selectByPrimaryKey(ms);
            //上个月单价
            if(pricesetAntList.size() > 0){
                Priceset pricesetAnt = new Priceset();
//                本月没有单价，上个月多个单价
                if(pricesetAntList.size() > 1){
                    String groupidL = expatriatesinfor.getGroup_id();
                    for(int i = 0; i < pricesetAntList.size();i ++){
                        if(groupidL.equals(pricesetAntList.get(i).getGroup_id())){
                            pricesetAntList.get(0).setPricesetgroup_id(pricesetList.get(i).getPricesetgroup_id());
                            BeanUtils.copyProperties(pricesetAntList.get(0), pricesetAnt);
                            pricesetList.add(pricesetAnt);
                        }
                    }
                    //本月没有单价，上个月1个单价
                }else{
                    pricesetAntList.get(0).setPricesetgroup_id(pricesetList.get(0).getPricesetgroup_id());
                    BeanUtils.copyProperties(pricesetAntList.get(0), pricesetAnt);
                    pricesetList.add(pricesetAnt);
                }
            }else{//上个月没有单价
                Expatriatesinfor expatr = expatriatesinforMapper.selectByPrimaryKey(ms);
                //判断传过来的月份 该员工是否为本月入场
                //前台查询的月份
                String pdone= pddate.substring(0,4);
                String pdtwo= pddate.substring(5);
                String pdstr= pdone + pdtwo;
                int pddateint = Integer.valueOf(pdstr);
                //入场月份
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String expatrdate = sdf.format(expatr.getAdmissiontime());
                String adone= expatrdate.substring(0,4);
                String adtwo= expatrdate.substring(5);
                String adstr= adone + adtwo;
                int adtimeint = Integer.valueOf(adstr);

                if(pddateint >= adtimeint){
                    Priceset pricesetEmpt = new Priceset();
                    pricesetEmpt.setGroup_id(expatriatesinfor.getGroup_id());
                    pricesetEmpt.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
                    pricesetEmpt.setUsername(expatriatesinfor.getExpname());
                    pricesetEmpt.setGraduation(expatriatesinfor.getGraduation_year());
                    pricesetEmpt.setCompany(expatriatesinfor.getSuppliername());
                    pricesetList.add(pricesetEmpt);
                }
            }
        }
        List<PricesetVo> rst = new ArrayList<PricesetVo>();
        PricesetVo a = new PricesetVo(new PricesetGroup(),new ArrayList<Priceset>());
        a.getMain().setPd_date(pddate);
        for(Priceset pr : pricesetList){
            a.getDetail().add(pr);
        }
        rst.add(a);
        return rst;
    }

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

        if(StrUtil.isNotBlank(pricesetVo.getMain().getPricesetgroup_id())){
            pricesetVo.getMain().preUpdate(tokenModel);
            pricesetGroupMapper.updateByPrimaryKeySelective(pricesetVo.getMain());
        }else{
            pricesetVo.getMain().preInsert(tokenModel);
            pricesetVo.getMain().setPricesetgroup_id(UUID.randomUUID().toString());
            pricesetGroupMapper.insert(pricesetVo.getMain());
        }

        for(Priceset priceset:pricesetVo.getDetail()){
            if(StrUtil.isNotBlank(priceset.getPriceset_id())){
                priceset.preUpdate(tokenModel);
                pricesetMapper.updateByPrimaryKeySelective(priceset);
            }else{
                priceset.preInsert(tokenModel);
                priceset.setPriceset_id(UUID.randomUUID().toString());
                priceset.setPricesetgroup_id(pricesetVo.getMain().getPricesetgroup_id());
                pricesetMapper.insert(priceset);
            }

        }
    }
}
