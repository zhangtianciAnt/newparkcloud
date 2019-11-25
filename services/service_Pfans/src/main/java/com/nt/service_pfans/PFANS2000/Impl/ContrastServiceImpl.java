package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Contrast;
import com.nt.service_pfans.PFANS2000.ContrastService;
import com.nt.service_pfans.PFANS2000.mapper.ContrastMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class ContrastServiceImpl implements ContrastService {

    @Autowired
    private ContrastMapper contrastMapper;


    @Override
    public List<Contrast> getList(Contrast contrast) throws Exception {
        return contrastMapper.select(contrast);
    }

}
