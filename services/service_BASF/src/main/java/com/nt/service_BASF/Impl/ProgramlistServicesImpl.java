package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Programlist;
import com.nt.dao_BASF.Usergroup;
import com.nt.dao_BASF.Usergroupdetailed;
import com.nt.dao_BASF.VO.UsergroupVo;
import com.nt.service_BASF.ProgramlistServices;
import com.nt.service_BASF.UsergroupServices;
import com.nt.service_BASF.mapper.ProgramlistMapper;
import com.nt.service_BASF.mapper.UsergroupMapper;
import com.nt.service_BASF.mapper.UsergroupdetailedMapper;
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
 * @ClassName: BASF21207ServicesImpl
 * @Author:
 * @Description: BASF培训清单模块Controller
 * @Date: 2019/11/4 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProgramlistServicesImpl implements ProgramlistServices {

    private static Logger log = LoggerFactory.getLogger(ProgramlistServicesImpl.class);

    @Autowired
    private ProgramlistMapper programlistMapper;

    /**
     * @param programlist
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<Programlist>
     * @Date 2019/11/4
     */
    @Override
    public List<Programlist> list(Programlist programlist) throws Exception {
        return programlistMapper.select(programlist);
    }

    /**
     * @param programlistid
     * @Method one
     * @Author
     * @Version 1.0
     * @Description 获取培训计划清单详情
     * @Return com.nt.dao_BASF.programlist
     * @Date 2019/11/25 14:16
     */
    @Override
    public Programlist one(String programlistid) throws Exception {
        return programlistMapper.selectByPrimaryKey(programlistid);
    }

    /**
     * @param programlist
     * @param tokenModel
     * @Method insert
     * @Author
     * @Version 1.0
     * @Description 创建培训清单
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public void insert(Programlist programlist, TokenModel tokenModel) throws Exception {
        programlist.preInsert(tokenModel);
        programlist.setProgramtype("BC039001");
        programlist.setProgramlistid(UUID.randomUUID().toString());
        programlistMapper.insert(programlist);
    }

    /**
     * @param programlist
     * @param tokenModel
     * @Method update
     * @Author
     * @Version 1.0
     * @Description 更新培训清单
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public void update(Programlist programlist, TokenModel tokenModel) throws Exception {
        programlist.preUpdate(tokenModel);
        programlistMapper.updateByPrimaryKeySelective(programlist);
    }

    /**
     * @param programlist
     * @Method Delete
     * @Author
     * @Version 1.0
     * @Description 删除培训清单
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public void delete(Programlist programlist) throws Exception {
        programlistMapper.updateByPrimaryKeySelective(programlist);
    }
}
