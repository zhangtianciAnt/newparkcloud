package com.nt.service_BASF.Impl;


import com.nt.dao_BASF.Program;
import com.nt.service_BASF.ProgramServices;
import com.nt.service_BASF.mapper.ProgramMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF21203ServicesImpl
 * @Author: WXL
 * @Description: BASF培训项目模块实现类
 * @Date: 2019/11/21 13:45
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProgramServicesImpl implements ProgramServices {

    private static Logger log = LoggerFactory.getLogger(ProgramServicesImpl.class);

    @Autowired
    private ProgramMapper programMapper;


    /**
     * @param Program
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取培训项目数据列表
     * @Return java.util.List<Program>
     * @Date 2019/11/21 13:50
     */
    @Override
    public List<Program> list() throws Exception {
        Program program = new Program();
        return programMapper.select(program);
    }


    /**
     * @param program
     * @param tokenModel
     * @Method insert
     * @Author WXL
     * @Version 1.0
     * @Description 创建培训项目数据
     * @Return void
     * @Date 2019/11/21 13:50
     */
    @Override
    public void insert(Program program, TokenModel tokenModel) throws Exception {
        program.preInsert(tokenModel);
        program.setProgramid(UUID.randomUUID().toString());
        programMapper.insert(program);
    }


    /**
     * @param program
     * @Method Delete
     * @Author WXL
     * @Version 1.0
     * @Description 删除培训项目数据
     * @Return void
     * @Date 2019/11/21 13：50
     */
    @Override
    public void delete(Program program) throws Exception {
        programMapper.updateByPrimaryKeySelective(program);
    }


    /**
     * @param programid
     * @Method one
     * @Author WXL
     * @Version 1.0
     * @Description 获取数据培训项目详情
     * @Return com.nt.dao_BASF.Course
     * @Date 2019/11/18 17：24
     */
    @Override
    public Program one(String programid) throws Exception {
        return programMapper.selectByPrimaryKey(programid);
    }


    /**
     * @param program
     * @param tokenModel
     * @Method update
     * @Author WXL
     * @Version 1.0
     * @Description 更新培训项目详情
     * @Return void
     * @Date 2019/11/21 13：51
     */
    @Override
    public void update(Program program, TokenModel tokenModel) throws Exception {
        program.preUpdate(tokenModel);
        programMapper.updateByPrimaryKey(program);
    }

}
