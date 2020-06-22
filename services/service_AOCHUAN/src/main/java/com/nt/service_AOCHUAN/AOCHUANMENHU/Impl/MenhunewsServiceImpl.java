package com.nt.service_AOCHUAN.AOCHUANMENHU.Impl;


import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.dao_AOCHUAN.AOCHUANMENHU.Newsinformation;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhunewsService;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhuproductsService;
import com.nt.service_AOCHUAN.AOCHUANMENHU.mapper.MenhunewsMapper;
import com.nt.service_AOCHUAN.AOCHUANMENHU.mapper.MenhuproductsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class MenhunewsServiceImpl implements MenhunewsService {

    @Autowired
    private MenhunewsMapper menhunewsMapper;


    @Override
    public List<Newsinformation> get(Newsinformation newsinformation) throws Exception {
        return menhunewsMapper.select(newsinformation);
    }

    @Override
    public void insert(Newsinformation newsinformation, TokenModel tokenModel) throws Exception {
        newsinformation.preInsert(tokenModel);
        newsinformation.setNewsinformation_id(UUID.randomUUID().toString());
//        如果是一月 背景颜色是红色
        if(newsinformation.getNewsyearmon().substring(5,7).equals("01")){
            newsinformation.setBackgroundcolor("redbackgrounddate");
        }

        String yearmon = newsinformation.getNewsyearmon().substring(0,7);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = simpleDateFormat.parse(yearmon);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        String b = simpleDateFormat.format(calendar.getTime());

        newsinformation.setNewsyearmon(b);
        menhunewsMapper.insert(newsinformation);
    }

    @Override
    public Newsinformation One(String ids) throws Exception {
        return menhunewsMapper.selectByPrimaryKey(ids);
    }

    @Override
    public void delete(String id) throws Exception {
        menhunewsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Newsinformation newsinformation, TokenModel tokenModel) throws Exception {
        newsinformation.preUpdate(tokenModel);
        if(newsinformation.getNewsyearmon().substring(5,7).equals("01")){
            newsinformation.setBackgroundcolor("redbackgrounddate");
        }
        String yearmon = newsinformation.getNewsyearmon().substring(0,7);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = simpleDateFormat.parse(yearmon);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        String b = simpleDateFormat.format(calendar.getTime());

        newsinformation.setNewsyearmon(b);
        menhunewsMapper.updateByPrimaryKey(newsinformation);
    }
}
