package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.InterviewRecord;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface InterviewRecordService {

    //查看
    List<InterviewRecord> getInterviewRecord(InterviewRecord interviewrecord)throws Exception;

    public InterviewRecord One(String interviewrecord_id)throws  Exception;

    //创建
    public void insert(InterviewRecord interviewrecord, TokenModel tokenModel)throws  Exception;

    //修改
    public void updateInterviewRecord(InterviewRecord interviewrecord,TokenModel tokenModel)throws  Exception;




}
