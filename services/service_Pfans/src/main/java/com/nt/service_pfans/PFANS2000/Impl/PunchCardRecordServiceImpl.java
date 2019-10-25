package com.nt.service_pfans.PFANS2000.Impl;
import com.nt.dao_Pfans.PFANS2000.PunchCardRecord;
import com.nt.service_pfans.PFANS2000.PunchCardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.PunchCardRecordMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class PunchCardRecordServiceImpl implements PunchCardRecordService {

        @Autowired
        private PunchCardRecordMapper PunchCardRecordMapper;

        @Override
        public List<PunchCardRecord> getPunchCardRecord(PunchCardRecord punchcardrecord) throws Exception {
            return punchcardrecordMapper.select(punchcardrecord);
        }

        @Override
        public PunchCardRecord One(String punchcardrecordid) throws Exception {
            return punchcardrecordMapper.selectByPrimaryKey(punchcardrecordid);
        }

        @Override
        public void insertPunchCardRecord(PunchCardRecord punchcardrecord, TokenModel tokenModel) throws Exception {
            if (!StringUtils.isEmpty(punchcardrecord)) {
                punchcardrecord.preInsert(tokenModel);
                punchcardrecord.setPunchCardRecordid(UUID.randomUUID().toString());
                punchcardrecordMapper.insert(punchcardrecord);
            }
        }

        @Override
        public void updatePunchCardRecord(PunchCardRecord punchcardrecord, TokenModel tokenModel) throws Exception {
            if (!StringUtils.isEmpty(punchcardrecord)) {
                punchcardrecord.preUpdate(tokenModel);
                punchcardrecordMapper.updateByPrimaryKeySelective(punchcardrecord);
            }
        }
    }
}

