package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.GoalManagement;
import com.nt.service_pfans.PFANS2000.GoalManagementService;
import com.nt.service_pfans.PFANS2000.mapper.GoalManagementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class GoalManagementServiceImpl implements GoalManagementService {

    @Autowired
    private GoalManagementMapper goalmanagementMapper;

    @Override
    public void insert(GoalManagement goalmanagement, TokenModel tokenModel) throws Exception {
        goalmanagement.preInsert(tokenModel);
        goalmanagement.setGoalmanagement_id(UUID.randomUUID().toString());
        goalmanagementMapper.insert(goalmanagement);
    }
}
