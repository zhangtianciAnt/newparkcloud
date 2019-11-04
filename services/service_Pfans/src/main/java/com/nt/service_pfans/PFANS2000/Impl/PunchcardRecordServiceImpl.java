package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.PunchcardRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class PunchcardRecordServiceImpl implements PunchcardRecordService {

    @Autowired
    private PunchcardRecordMapper punchcardrecordMapper;

    @Override
    public List<PunchcardRecord> list(PunchcardRecord punchcardrecord) throws Exception {
        return punchcardrecordMapper.select(punchcardrecord);
    }

}
