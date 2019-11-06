package com.nt.service_BASF;

import com.nt.dao_BASF.User;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF10102Services
 * @Author: LXY
 * @Description: 人员通讯录模块接口
 * @Date: 2019/11/6 22.51
 * @Version: 1.0
 */
public interface UserService {
    List<Emailmessage> get(User user) throws Exception;

    void insert(TokenModel tokenModel, User user) throws Exception;

    void update(TokenModel tokenModel, User user) throws Exception;

    void del(TokenModel tokenModel, User user) throws Exception;
}
