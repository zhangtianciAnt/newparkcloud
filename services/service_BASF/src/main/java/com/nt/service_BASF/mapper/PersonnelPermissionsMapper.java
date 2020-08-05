package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.PersonnelPermissions;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PersonnelPermissionsMapper extends MyMapper<PersonnelPermissions> {
    List<PersonnelPermissions> selectByClass();
}
