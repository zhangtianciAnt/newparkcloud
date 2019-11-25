package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.GivingVo;
import com.nt.service_pfans.PFANS2000.GivingListService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class GivingListServiceImpl implements GivingListService {

    @Autowired
    private GivingMapper givingMapper;

    @Autowired
    private BaseMapper baseMapper;


    @Autowired
    private ContrastMapper contrastMapper;


    @Autowired
    private CasgiftApplyMapper casgiftapplyMapper;

    @Autowired
    private OtherTwoMapper othertwoMapper;

    @Autowired
    private OtherFiveMapper otherfiveMapper;

    @Autowired
    private AppreciationMapper appreciationMapper;

    @Override
    public GivingVo List(String giving_id) throws Exception {
        GivingVo GivingVo = new GivingVo();
        Giving Giving = new Giving();
        Giving.setGiving_id(giving_id);
        GivingVo.setGiving(Giving);
        OtherTwo othertwo = new OtherTwo();
        othertwo.setGiving_id(giving_id);
        List<OtherTwo> othertwolist  = othertwoMapper.select(othertwo);
        othertwolist = othertwolist.stream().sorted(Comparator.comparing(OtherTwo::getRowindex)).collect(Collectors.toList());
        GivingVo.setOtherTwo(othertwolist);
        Appreciation appreciation = new Appreciation();
        appreciation.setGiving_id(giving_id);
        List<Appreciation> appreciationlist  = appreciationMapper.select(appreciation);
        appreciationlist = appreciationlist.stream().sorted(Comparator.comparing(Appreciation::getRowindex)).collect(Collectors.toList());
        GivingVo.setAppreciation(appreciationlist);
        OtherFive otherfive = new OtherFive();
        otherfive.setGiving_id(giving_id);
        List<OtherFive> otherfivelist  = otherfiveMapper.select(otherfive);
        otherfivelist = otherfivelist.stream().sorted(Comparator.comparing(OtherFive::getRowindex)).collect(Collectors.toList());
        GivingVo.setOtherFive(otherfivelist);
        return GivingVo;
    }

}
