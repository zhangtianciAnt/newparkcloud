package com.nt.service_pfans.PFANS3000.Impl;

import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.dao_Pfans.PFANS3000.UseCoupon;
import com.nt.dao_Pfans.PFANS3000.Vo.JapanCondominiumVo;
import com.nt.service_pfans.PFANS3000.JapanCondominiumService;
import com.nt.service_pfans.PFANS3000.mapper.JapanCondominiumMapper;
import com.nt.service_pfans.PFANS3000.mapper.UseCouponMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;


@Service
@Transactional(rollbackFor=Exception.class)
public class JapanCondominiumServiceImpl implements JapanCondominiumService {

    @Autowired
    private JapanCondominiumMapper japancondominiumMapper;
    @Autowired
    private UseCouponMapper usecouponMapper;

    @Override
    public List<JapanCondominium> getJapanCondominium(JapanCondominium japancondominium) throws Exception {
        return japancondominiumMapper.select(japancondominium);
    }

    @Override
    public List<JapanCondominium> getJapanCondominiumlist(JapanCondominium japancondominium) throws Exception {
        return japancondominiumMapper.select(japancondominium);
    }
    //按id查询
    @Override
    public JapanCondominium One(String japancondominiumid) throws Exception {
        if (japancondominiumid.equals("")) {
            return null;
        }
        return japancondominiumMapper.selectByPrimaryKey(japancondominiumid);
    }
    //按id查询
    @Override
    public JapanCondominiumVo selectById(String japancondominiumid) throws Exception {
        JapanCondominiumVo japanVo = new JapanCondominiumVo();
        UseCoupon usecoupon = new UseCoupon();
        usecoupon.setJapancondominiumid(japancondominiumid);
        List<UseCoupon> usecouponlist = usecouponMapper.select(usecoupon);
        JapanCondominium Japan = japancondominiumMapper.selectByPrimaryKey(japancondominiumid);
        japanVo.setJapancondominium(Japan);
        japanVo.setUsecoupon(usecouponlist);
        return japanVo;
    }

    @Override
    public void insertJapanCondominium(JapanCondominium japancondominium, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(japancondominium)){
            japancondominium.preInsert(tokenModel);
            japancondominium.setJapancondominiumid(UUID.randomUUID().toString());
            japancondominiumMapper.insert(japancondominium);
        }
    }


    @Override
    public void updateJapanCondominium(JapanCondominium japancondominium, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(japancondominium)){
            japancondominium.preUpdate(tokenModel);
            japancondominiumMapper.updateByPrimaryKey(japancondominium);
        }
    }

    //新建
    @Override
    public void insertJapanCondominiumVo(JapanCondominiumVo japancondominiumVo, TokenModel tokenModel) throws Exception {
        String japancondominiumid = UUID.randomUUID().toString();
        JapanCondominium japancondominium = new JapanCondominium();
        BeanUtils.copyProperties(japancondominiumVo.getJapancondominium(), japancondominium);
        japancondominium.preInsert(tokenModel);
        japancondominium.setJapancondominiumid(japancondominiumid);
        japancondominiumMapper.insertSelective(japancondominium);

        List<UseCoupon> usecouponlist = japancondominiumVo.getUsecoupon();
        if (usecouponlist != null) {
            for (UseCoupon usecoupon : usecouponlist) {
                usecoupon.preInsert(tokenModel);
                usecoupon.setUsecouponid(UUID.randomUUID().toString());
                usecoupon.setJapancondominiumid(japancondominiumid);
                usecouponMapper.insertSelective(usecoupon);
            }
        }
    }
    //更新
    @Override
    public void updateJapanCondominiumVo(JapanCondominiumVo japancondominiumVo, TokenModel tokenModel) throws Exception {
        JapanCondominium japancondominium = new JapanCondominium();
        BeanUtils.copyProperties(japancondominiumVo.getJapancondominium(), japancondominium);
        japancondominium.preUpdate(tokenModel);
        japancondominiumMapper.updateByPrimaryKey(japancondominium);
        List<UseCoupon> usecouponlist = japancondominiumVo.getUsecoupon();
        if (usecouponlist != null) {
            for (UseCoupon usecoupon : usecouponlist) {
                usecoupon.preUpdate(tokenModel);
                usecouponMapper.updateByPrimaryKey(usecoupon);
            }
        }
    }
}
