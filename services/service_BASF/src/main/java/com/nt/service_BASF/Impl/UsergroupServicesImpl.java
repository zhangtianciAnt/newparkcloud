package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Usergroup;
import com.nt.dao_BASF.Usergroupdetailed;
import com.nt.dao_BASF.VO.UsergroupVo;
import com.nt.service_BASF.UsergroupServices;
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
 * @ClassName: BASF10103ServicesImpl
 * @Author: SUN
 * @Description: BASF用户组模块实现类
 * @Date: 2019/11/4 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UsergroupServicesImpl implements UsergroupServices {

    private static Logger log = LoggerFactory.getLogger(UsergroupServicesImpl.class);

    @Autowired
    private UsergroupMapper usergroupMapper;
    @Autowired
    private UsergroupdetailedMapper usergroupdetailedMapper;

    /**
     * @param usergroup
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<Usergroup>
     * @Date 2019/11/4
     */
    @Override
    public List<Usergroup> list(Usergroup usergroup) throws Exception {
        return usergroupMapper.select(usergroup);
    }

    /**
     * @param usergroupdetailed
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<Usergroupdetailed>
     * @Date 2019/11/5
     */
    @Override
    public List<Usergroupdetailed> getDetailedList(Usergroupdetailed usergroupdetailed) throws Exception {
        return usergroupdetailedMapper.select(usergroupdetailed);
    }

    /**
     * @param usergroupVo
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<usergroupVo>
     * @Date 2019/11/5
     */
    @Override
    public void insert(TokenModel tokenModel, UsergroupVo usergroupVo) throws Exception {
        Usergroup usergroup = new Usergroup();
        usergroup.preInsert(tokenModel);
        String id = UUID.randomUUID().toString();
        usergroup.setUsergroupid(id);
        usergroup.setRemark(usergroupVo.getRemark());
        usergroup.setUsergroupname(usergroupVo.getUsergroupname());
        usergroupMapper.insert(usergroup);

        if(usergroupVo.getTeammember().size()>0)
        {
            for(int i=0;i<usergroupVo.getTeammember().size();i++)
            {
                Usergroupdetailed usergroupdetailed = usergroupVo.getTeammember().get(i);
                usergroupdetailed.preInsert(tokenModel);
                usergroupdetailed.setUsergroupdetailedid(UUID.randomUUID().toString());
                usergroupdetailed.setUsergroupid(id);
                usergroupdetailed.setTeammember(usergroupVo.getTeammember().get(i).getUserid());
                usergroupdetailedMapper.insert(usergroupdetailed);
            }
        }
    }

    /**
     * @param usergroupVo
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<usergroupVo>
     * @Date 2019/11/5
     */
    @Override
    public void update(TokenModel tokenModel, UsergroupVo usergroupVo) throws Exception {
        Usergroup usergroup = new Usergroup();
        usergroup.preUpdate(tokenModel);
        usergroup.setUsergroupid(usergroupVo.getUsergroupid());
        usergroup.setRemark(usergroupVo.getRemark());
        usergroup.setUsergroupname(usergroupVo.getUsergroupname());
        usergroupMapper.updateByPrimaryKeySelective(usergroup);

        Usergroupdetailed usergroupdetailed = new Usergroupdetailed();
        usergroupdetailed.setUsergroupid(usergroupVo.getUsergroupid());
        usergroupdetailedMapper.delete(usergroupdetailed);


        if(usergroupVo.getTeammember().size()>0)
        {
            for(int i=0;i<usergroupVo.getTeammember().size();i++)
            {
                Usergroupdetailed usergroupdetailedup = usergroupVo.getTeammember().get(i);
                usergroupdetailedup.preInsert(tokenModel);
                usergroupdetailedup.setUsergroupdetailedid(UUID.randomUUID().toString());
                usergroupdetailedup.setUsergroupid(usergroupVo.getUsergroupid());
                usergroupdetailedup.setTeammember(usergroupVo.getTeammember().get(i).getUserid());
                usergroupdetailedMapper.insert(usergroupdetailedup);
            }
        }
    }

    @Override
    public void delete(TokenModel tokenModel,Usergroup usergroup) throws Exception {
        //逻辑删除（status -> "1"）
        usergroupMapper.updateByPrimaryKeySelective(usergroup);
    }
}
