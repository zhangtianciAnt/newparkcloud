package com.nt.service_BASF;


import com.nt.dao_BASF.Program;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF21203Services
 * @Author: WXL
 * @Description:数据监控管理接口
 * @Date: 2019/11/21 13:34
 * @Version: 1.0
 */
public interface ProgramServices {

    //获取培训项目列表
    List<Program> list() throws Exception;

    //创建培训项目
    void insert(Program program, TokenModel tokenModel) throws Exception;

    //删除培训项目
    void delete(Program program) throws Exception;

    //获取培训项目据详情
    Program one(String programid) throws Exception;

    //更新课程数据
    void update(Program program, TokenModel tokenModel) throws Exception;

}
