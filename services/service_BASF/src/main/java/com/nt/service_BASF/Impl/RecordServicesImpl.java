package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Record;
import com.nt.service_BASF.RecordServices;
import com.nt.service_BASF.mapper.RecordMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: RecordServicesImpl
 * @Author: Newtouch
 * @Description: 人员培训记录接口实现类
 * @Date: 2019/11/25 13:49
 * @Version: 1.0
 */
@Service
public class RecordServicesImpl implements RecordServices {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public List<Record> list() throws Exception {
        return recordMapper.select(new Record());
    }

    @Override
    public void insert(Record record, TokenModel tokenModel) throws Exception {
        record.preInsert(tokenModel);
        record.setRecordid(UUID.randomUUID().toString());
        recordMapper.insert(record);
    }

    @Override
    public void delete(Record record) throws Exception {
        //逻辑删除（status -> "1"）
        recordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Record one(String recordid) throws Exception {
        return recordMapper.selectByPrimaryKey(recordid);
    }

    @Override
    public void update(Record record, TokenModel tokenModel) throws Exception {
        record.preUpdate(tokenModel);
        recordMapper.updateByPrimaryKey(record);
    }

}
