package com.nt.service_pfans.PFANS2000.Impl;
import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.service_pfans.PFANS2000.OvertimeService;
import com.nt.service_pfans.PFANS2000.mapper.OvertimeMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class OvertimeServiceImpl implements OvertimeService {

    @Autowired
    private OvertimeMapper overtimeMapper;

    @Override
    public List<Overtime> getOvertime() throws Exception {
        if(overtimeMapper.getOvertime().isEmpty()){
            return null;
        }
        return overtimeMapper.getOvertime();
    }


    @Override
    public Overtime getOvertimeOne(String overtime_id) throws Exception {
        if(overtime_id.equals(" ")){
            return  null;
        }
        return overtimeMapper.selectByPrimaryKey(overtime_id);

    }


    @Override
    public void insertOvertime(Overtime overtime, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(overtime)){
            overtime.preInsert(tokenModel);
            overtime.setOvertime_id(UUID.randomUUID().toString());
            overtimeMapper.insertSelective(overtime);
        }
    }

    @Override
    public void updateOvertime(Overtime overtime, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(overtime)){
            overtime.preUpdate(tokenModel);
            overtimeMapper.updateByPrimaryKeySelective(overtime);
            }
        }
    }
