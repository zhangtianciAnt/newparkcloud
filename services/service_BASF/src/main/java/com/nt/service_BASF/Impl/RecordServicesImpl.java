package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Program;
import com.nt.dao_BASF.Record;
import com.nt.service_BASF.RecordServices;
import com.nt.service_BASF.mapper.ProgramMapper;
import com.nt.service_BASF.mapper.RecordMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private ProgramMapper programMapper;

    @Override
    public List<Record> list(Record record) throws Exception {
//        Record record = new Record();
        record.setStatus("0");
        Program program = programMapper.selectByPrimaryKey(record.getProgramid());
        int num = recordMapper.selectCount(record);
        List<Record> list1=new ArrayList();
        list1 = recordMapper.select(record);
        int i = 0;
        for(Record record1:list1){
            if(record1.getIfmakeup() == "1"){
                i = i+1;
            }
        }
        Double per =  Double.valueOf(i) /Double.valueOf(num)*100;
        String a = per + "%";
        program.setPassrate(a);

        program.setActualpeo(num);
        programMapper.updateByPrimaryKeySelective(program);
        return recordMapper.select(record);
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
