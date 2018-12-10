package com.nt.service_Org;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.PersonalCenterVo;
/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Org
 * @ClassName: 基本信息的相关Services
 * @Description: java类作用描述
 * @Author: FEIJIALIANG
 * @CreateDate: 2018/12/05
 * @Version: 1.0
 */
public interface PersonalCenterVoService {
    // 获取基本信息
    PersonalCenterVo get(String userid) throws Exception;
    // 更新或保存基本信息
    void save(CustomerInfo customerInfo) throws Exception;
}
