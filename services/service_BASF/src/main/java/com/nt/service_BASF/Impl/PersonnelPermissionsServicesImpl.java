package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.PersonnelPermissions;
import com.nt.service_BASF.PersonnelPermissionsServices;
import com.nt.service_BASF.mapper.PersonnelPermissionsMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF90905ServicesImpl
 * @Author: GJ
 * @Description: BASF人员清点模块实现类
 * @Date: 2020/03/30 16:40
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PersonnelPermissionsServicesImpl implements PersonnelPermissionsServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private PersonnelPermissionsMapper personnelPermissionsMapper;

    /**
     * @param personnelPermissions
     * @Method list
     * @Author GJ
     * @Version 1.0
     * @Description 获取人员部门列表
     * @Return java.util.List<PersonnelPermissions>
     * @Date 2020/03/30 16:42
     */
    @Override
    public List<PersonnelPermissions> list() throws Exception {
        PersonnelPermissions personnelPermissions = new PersonnelPermissions();
        return personnelPermissionsMapper.select(personnelPermissions);
    }

    /**
     * @param personnelPermissions
     * @Method list
     * @Author GJ
     * @Version 1.0
     * @Description 保存部门列表
     * @Return java.util.List<PersonnelPermissions>
     * @Date 2020/03/30 16:42
     */
    @Override
    public void insert(ArrayList<PersonnelPermissions> personnelPermissions, TokenModel tokenModel) throws Exception {
        PersonnelPermissions personnelPermissions1 = new PersonnelPermissions();
        personnelPermissionsMapper.delete(personnelPermissions1);

        for(int i = 0;i<personnelPermissions.size();i++)
        {
            personnelPermissions.get(i).preInsert(tokenModel);
            personnelPermissions.get(i).setPermissionsid(UUID.randomUUID().toString());
            personnelPermissionsMapper.insert(personnelPermissions.get(i));
        }
    }

}
