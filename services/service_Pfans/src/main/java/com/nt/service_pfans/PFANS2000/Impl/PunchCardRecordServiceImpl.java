package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.PunchCardRecord;
import com.nt.service_pfans.PFANS2000.PunchCardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.PunchCardRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class PunchCardRecordServiceImpl implements PunchCardRecordService {
    @Autowired
    private PunchCardRecordMapper punchcardrecordMapper;

    @Override
    public List<PunchCardRecord> getPunchCardRecord(PunchCardRecord punchcardrecord) throws Exception {
        return punchcardrecordMapper.select(punchcardrecord);
    }

    @Override
    public PunchCardRecord One(String punchcardrecordid) throws Exception {
        return punchcardrecordMapper.selectByPrimaryKey(punchcardrecordid);
    }
}
