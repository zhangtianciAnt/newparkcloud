package com.nt.service_BASF;

import com.nt.dao_BASF.Usergroup;
import com.nt.dao_BASF.Usergroupdetailed;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF10103Services
 * @Author: SUN
 * @Description: BASF用户组模块接口
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
public interface UsergroupServices {

    //获取用户组列表
    List<Usergroup> list(Usergroup usergroup) throws Exception;

    //获取用户组明细列表
    List<Usergroupdetailed> getDetailedList(Usergroupdetailed usergroupdetailed) throws Exception;
}
