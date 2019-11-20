package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.InterviewRecord;
import com.nt.service_pfans.PFANS2000.InterviewRecordService;
import com.nt.service_pfans.PFANS2000.mapper.InterviewRecordMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class InterviewRecordServiceImpl implements InterviewRecordService {

    @Autowired
    private InterviewRecordMapper interviewrecordMapper;

    @Override
    public List<InterviewRecord> getInterviewRecord(InterviewRecord interviewrecord) {
        return interviewrecordMapper.select(interviewrecord);
    }

    @Override
    public InterviewRecord One(String interviewrecord_id) throws Exception{
        return interviewrecordMapper.selectByPrimaryKey(interviewrecord_id);
    }

    @Override
    public void updateInterviewRecord(InterviewRecord interviewrecord, TokenModel tokenModel) throws Exception {
        interviewrecord.preUpdate(tokenModel);
        interviewrecordMapper.updateByPrimaryKey(interviewrecord);
    }

    @Override
    public void insert(InterviewRecord interviewrecord, TokenModel tokenModel) throws Exception {
        interviewrecord.preInsert(tokenModel);
        interviewrecord.setInterviewrecord_id(UUID.randomUUID().toString()); ;
        interviewrecordMapper.insert(interviewrecord);
    }
}
