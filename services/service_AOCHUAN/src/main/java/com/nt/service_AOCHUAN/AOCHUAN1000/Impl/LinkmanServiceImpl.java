package com.nt.service_AOCHUAN.AOCHUAN1000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Linkman;
import com.nt.service_AOCHUAN.AOCHUAN1000.LinkmanService;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.LinkmanMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class LinkmanServiceImpl implements LinkmanService {
    @Autowired
    private LinkmanMapper linkmanMapper;

    @Override
    public List<Linkman> get() throws Exception {
        return linkmanMapper.selectAll();
    }

    @Override
    public List<Linkman> getByBaseinforId(String baseinfor_id) throws Exception {
        return linkmanMapper.selectBybaseinfor_id(baseinfor_id);
    }

    @Override
    public Linkman getOne(String id) throws Exception {
        return linkmanMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Linkman linkman, TokenModel tokenModel) throws Exception {
        linkman.preUpdate(tokenModel);
        linkmanMapper.updateByPrimaryKeySelective(linkman);
    }

    @Override
    public void insert(Linkman linkman, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        linkman.setLinkman_id(id);
        linkman.preInsert(tokenModel);
        linkmanMapper.insert(linkman);
    }

    @Override
    public void delete(String id) throws Exception {
        linkmanMapper.deleteByPrimaryKey(id);
    }
}
