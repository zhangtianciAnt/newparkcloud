package com.nt.service_BASF;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Usergroup;
import com.nt.dao_BASF.Usergroupdetailed;
import com.nt.dao_BASF.Fireaccidentrecord;
import com.nt.dao_BASF.VO.UsergroupVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF10203Services
 * @Author: SUN
 * @Description: BASF消防事故记录模块接口
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
public interface FireaccidentrecordServices {

    //获取消防事故记录列表
    List<Fireaccidentrecord> list(Fireaccidentrecord fireaccidentrecord) throws Exception;

    //获取消防事故记录详情
    Fireaccidentrecord selectById(String fireaccidentrecordid) throws Exception;

    //insert消防事故记录
    void insert(TokenModel tokenModel, Fireaccidentrecord fireaccidentrecord) throws Exception;

    //update消防事故记录
    void update(TokenModel tokenModel, Fireaccidentrecord fireaccidentrecord) throws Exception;
}
