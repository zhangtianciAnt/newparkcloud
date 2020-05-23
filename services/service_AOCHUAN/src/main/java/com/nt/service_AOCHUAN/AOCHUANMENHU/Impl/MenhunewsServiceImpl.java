package com.nt.service_AOCHUAN.AOCHUANMENHU.Impl;


import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.dao_AOCHUAN.AOCHUANMENHU.Newsinformation;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhunewsService;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhuproductsService;
import com.nt.service_AOCHUAN.AOCHUANMENHU.mapper.MenhunewsMapper;
import com.nt.service_AOCHUAN.AOCHUANMENHU.mapper.MenhuproductsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class MenhunewsServiceImpl implements MenhunewsService {

    @Autowired
    private MenhunewsMapper menhunewsMapper;


    @Override
    public List<Newsinformation> get(Newsinformation newsinformation) throws Exception {
        return menhunewsMapper.select(newsinformation);
    }
}
