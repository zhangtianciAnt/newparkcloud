package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
import com.nt.service_pfans.PFANS2000.mapper.StaffexitprocedureMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class StaffexitprocedureServiceImpl implements StaffexitprocedureService {

    @Autowired
    private StaffexitprocedureMapper staffexitprocedureMapper;

    //列表查询
    @Override
    public List<Staffexitprocedure> get(Staffexitprocedure staffexitprocedure) throws Exception {
        return staffexitprocedureMapper.select(staffexitprocedure);
    }

    //新建
    @Override
    public void create(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception {
        if (!(staffexitprocedure.equals(null) || staffexitprocedure.equals(""))) {
            staffexitprocedure.preInsert(tokenModel);
            staffexitprocedure.setStaffexitprocedure_id(UUID.randomUUID().toString());
            staffexitprocedureMapper.insertSelective(staffexitprocedure);
        }
    }

    //编辑
    @Override
    public void update(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception {
        staffexitprocedureMapper.updateByPrimaryKey(staffexitprocedure);
    }

    //按id查询
    @Override
    public Staffexitprocedure one(String staffexitprocedure_id) throws Exception {
        if (staffexitprocedure_id.equals("")) {
            return null;
        }
        return staffexitprocedureMapper.selectByPrimaryKey(staffexitprocedure_id);
    }
}
