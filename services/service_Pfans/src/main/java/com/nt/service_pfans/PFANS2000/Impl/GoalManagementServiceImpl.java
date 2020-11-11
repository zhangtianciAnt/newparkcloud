package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.GoalManagement;
import com.nt.service_Org.UserService;
import com.nt.service_pfans.PFANS2000.GoalManagementService;
import com.nt.service_pfans.PFANS2000.mapper.GoalManagementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class GoalManagementServiceImpl implements GoalManagementService {

    @Autowired
    private GoalManagementMapper goalmanagementMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<GoalManagement> list(GoalManagement goalManagement ) throws Exception {
        SimpleDateFormat sfym = new SimpleDateFormat("yyyy-MM");
        Calendar calend = Calendar.getInstance();
        calend.setTime(new Date());
        String yearmm  = sfym.format(calend.getTime());
        String years = yearmm.substring(0,4);
        String month = yearmm.substring(5,7);
        if(Integer.valueOf(month)<3)
        {
            years = String.valueOf(Integer.valueOf(years)-1);
        }
        goalManagement.setYears(years);
        List<GoalManagement> goalManagementList = goalmanagementMapper.select(goalManagement);
        List<CustomerInfo> customerInfoList = userService.getCustomerInfoResign(years);
        for(CustomerInfo c:customerInfoList)
        {
            goalManagementList = goalManagementList.stream().filter(item -> (!item.getUser_id().equals(c.getUserid()))).collect(Collectors.toList());
        }
        return goalManagementList;
    }
    @Override
    public GoalManagement One(String goalmanagement_id) throws Exception {

        GoalManagement log   =goalmanagementMapper.selectByPrimaryKey(goalmanagement_id);
        return log;
    }

    @Override
    public List<GoalManagement> yearsCheck(GoalManagement goalManagement) throws Exception {
        return goalmanagementMapper.yearsCheck(goalManagement);
    }

    @Override
    public void upd (GoalManagement goalManagement, TokenModel tokenModel) throws Exception {
        goalManagement.preUpdate(tokenModel);
        goalmanagementMapper.updateByPrimaryKeySelective(goalManagement);
    }
    @Override
    public void insert(GoalManagement goalmanagement, TokenModel tokenModel) throws Exception {
        goalmanagement.preInsert(tokenModel);
        goalmanagement.setGoalmanagement_id(UUID.randomUUID().toString());
        goalmanagementMapper.insert(goalmanagement);
    }
}
