package com.nt.service_BASF;

import com.nt.dao_BASF.Record;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: RecordServices
 * @Author: 王哲
 * @Description: 人员培训记录接口类
 * @Date: 2019/11/25 13:48
 * @Version: 1.0
 */
public interface RecordServices {

    //获取人员培训记录列表
    List<Record> list(Record record) throws Exception;

    //创建人员培训记录
    void insert(Record record, TokenModel tokenModel) throws Exception;

    //删除人员培训记录
    void delete(Record record) throws Exception;

    //获取人员培训记录详情
    Record one(String questionid) throws Exception;

    //更新人员培训记录
    void update(Record record, TokenModel tokenModel) throws Exception;
}
