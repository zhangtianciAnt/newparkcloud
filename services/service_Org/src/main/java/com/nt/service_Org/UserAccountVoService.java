package com.nt.service_Org;
import com.nt.dao_Org.Vo.UserAccountVo;
/**
        * @ProjectName: newparkcloud
        * @Package: com.nt.service_Org
        * @ClassName: 修改密码的相关Services
        * @Description: java类作用描述
        * @Author: WANGSHUAI
        * @CreateDate: 2018/11/05
        * @Version: 1.0
        */
public interface UserAccountVoService {
   //获取密码加以验证
   void confirmPassword (UserAccountVo userAccountVo) throws Exception;
}
