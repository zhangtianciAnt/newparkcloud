package com.nt.service_pfans.PFANS2000.Impl;


import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.service_pfans.PFANS2000.mapper.WagesMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class WagesServiceImpl implements WagesService {

    @Autowired
    private WagesMapper wagesMapper;
}
