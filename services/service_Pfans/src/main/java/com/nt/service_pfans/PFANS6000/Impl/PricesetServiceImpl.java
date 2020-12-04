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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        String pricesetGroupid = pricesetGroupMapper.select(pricesetGroup).get(0).getPricesetgroup_id();
        Priceset priceset = new Priceset();
        priceset.setGroup_id(groupid);
        priceset.setPricesetgroup_id(pricesetGroupid);
        List<Priceset> pricesetList = pricesetMapper.select(priceset);
        for (Priceset pr : pricesetList) {
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
