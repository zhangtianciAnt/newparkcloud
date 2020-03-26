package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Unusedevice;
import com.nt.service_pfans.PFANS1000.UnusedeviceService;
import com.nt.service_pfans.PFANS1000.mapper.UnusedeviceMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class UnusedeviceServiceImpl implements UnusedeviceService {

    @Autowired
    private UnusedeviceMapper unusedeviceMapper;


    @Override
    public List<Unusedevice> getUnusedevice(Unusedevice unusedevice) throws Exception {
        return unusedeviceMapper.select(unusedevice);
    }

    @Override
    public List<Unusedevice> selectUnusedevice() throws Exception {
        return unusedeviceMapper.selectUnusedevice();
    }


    @Override
    public Unusedevice One(String unusedeviceid) throws Exception {
        return unusedeviceMapper.selectByPrimaryKey(unusedeviceid);
    }

    @Override
    public void update(Unusedevice unusedevice, TokenModel tokenModel) throws Exception {
        unusedeviceMapper.updateByPrimaryKeySelective(unusedevice);
    }

    @Override
    public void insert(Unusedevice unusedevice, TokenModel tokenModel) throws Exception {
        String unusedeviceid = UUID.randomUUID().toString();
        Integer rowindex = unusedeviceMapper.selectCount(unusedevice) + 1;
        unusedevice.preInsert(tokenModel);
        unusedevice.setUnusedeviceid(unusedeviceid);
        unusedeviceMapper.insertSelective(unusedevice);
    }

    @Override
    public List<Unusedevice> getUnusedeviceList(Unusedevice unusedevice, HttpServletRequest request) throws Exception {
        return unusedeviceMapper.select(unusedevice);
    }
}
