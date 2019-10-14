package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Goalmanagement;
import com.nt.service_pfans.PFANS2000.GoalmanagementService;
import com.nt.service_pfans.PFANS2000.mapper.GoalmanagementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class GoalmanagementServiceImpl implements GoalmanagementService {

    @Autowired
    private GoalmanagementMapper goalmanagementMapper;

    @Override
    public void insert(Goalmanagement goalmanagement, TokenModel tokenModel) throws Exception {
        goalmanagement.preInsert(tokenModel);
        goalmanagement.setGoalmanagement_id(UUID.randomUUID().toString());
        goalmanagementMapper.insert(goalmanagement);
    }
}
