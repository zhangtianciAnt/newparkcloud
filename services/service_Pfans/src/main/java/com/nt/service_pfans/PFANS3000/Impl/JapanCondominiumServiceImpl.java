package com.nt.service_pfans.PFANS3000.Impl;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.dao_Pfans.PFANS3000.UseCoupon;
import com.nt.dao_Pfans.PFANS3000.Vo.JapanCondominiumVo;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS3000.JapanCondominiumService;
import com.nt.service_pfans.PFANS3000.mapper.JapanCondominiumMapper;
import com.nt.service_pfans.PFANS3000.mapper.UseCouponMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor=Exception.class)
public class JapanCondominiumServiceImpl implements JapanCondominiumService {

    @Autowired
    private JapanCondominiumMapper japancondominiumMapper;
    @Autowired
    private UseCouponMapper usecouponMapper;
    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public List<JapanCondominium> getJapanCondominium(JapanCondominium japancondominium) throws Exception {
        return japancondominiumMapper.select(japancondominium);
    }

    @Override
    public JapanCondominiumVo selectById(String japancondominiumid) throws Exception {
        JapanCondominiumVo japanVo = new JapanCondominiumVo();
        UseCoupon usecoupon = new UseCoupon();
        usecoupon.setJapancondominiumid(japancondominiumid);
        List<UseCoupon> usecouponlist = usecouponMapper.select(usecoupon);
        usecouponlist = usecouponlist.stream().sorted(Comparator.comparing(UseCoupon::getRowindex)).collect(Collectors.toList());
        JapanCondominium Japan = japancondominiumMapper.selectByPrimaryKey(japancondominiumid);
        japanVo.setJapancondominium(Japan);
        japanVo.setUsecoupon(usecouponlist);
        return japanVo;
    }

    @Override
    public void updateJapanCondominiumVo(JapanCondominiumVo japancondominiumVo, TokenModel tokenModel) throws Exception {
        JapanCondominium japancondominium = new JapanCondominium();
        BeanUtils.copyProperties(japancondominiumVo.getJapancondominium(), japancondominium);
        japancondominium.preUpdate(tokenModel);
        japancondominiumMapper.updateByPrimaryKey(japancondominium);
        String japancondominiumid = japancondominium.getJapancondominiumid();
        UseCoupon usec = new UseCoupon();
        usec.setJapancondominiumid(japancondominiumid);
        usecouponMapper.delete(usec);
        List<UseCoupon> usecouponlist = japancondominiumVo.getUsecoupon();
        if (usecouponlist != null) {
            int rowundex = 0;
            for (UseCoupon usecoupon : usecouponlist) {
                rowundex = rowundex + 1;
                usecoupon.preInsert(tokenModel);
                usecoupon.setUsecouponid(UUID.randomUUID().toString());
                usecoupon.setJapancondominiumid(japancondominiumid);
                usecoupon.setRowindex(rowundex);
                usecouponMapper.insertSelective(usecoupon);
                Dictionary dictionary = new Dictionary();
                dictionary.preInsert(tokenModel);
                dictionary.setCode(usecoupon.getCoupontype());
                dictionary.setValue2(usecoupon.getCouponnumber());
                dictionaryMapper.updateByPrimaryKeySelective(dictionary);
            }
        }
        Dictionary dictionary = new Dictionary();
        dictionary.preInsert(tokenModel);
        dictionary.setCode(japancondominium.getCondominiumcompany());
        dictionary.setValue2(japancondominium.getMoneys());
        dictionaryMapper.updateByPrimaryKeySelective(dictionary);
    }

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
            int rowundex = 0;
            for (UseCoupon usecoupon : usecouponlist) {
                rowundex = rowundex + 1;
                usecoupon.preInsert(tokenModel);
                usecoupon.setUsecouponid(UUID.randomUUID().toString());
                usecoupon.setJapancondominiumid(japancondominiumid);
                usecoupon.setRowindex(rowundex);
                usecouponMapper.insertSelective(usecoupon);
                Dictionary dictionary = new Dictionary();
                dictionary.preUpdate(tokenModel);
                dictionary.setCode(usecoupon.getCoupontype());
                dictionary.setValue2(usecoupon.getCouponnumber());
                dictionaryMapper.updateByPrimaryKeySelective(dictionary);
            }
        }
        Dictionary dictionary = new Dictionary();
        dictionary.preUpdate(tokenModel);
        dictionary.setCode(japancondominium.getCondominiumcompany());
        dictionary.setValue2(japancondominium.getMoneys());
        dictionaryMapper.updateByPrimaryKeySelective(dictionary);
    }

}
