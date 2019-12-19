package com.nt.service_BASF;

import com.nt.dao_BASF.DutySimt;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: DutySimtServices
 * @Author: WXZ
 * @Description: DutySimtServices
 * @Date: 2019/12/19 15:04
 * @Version: 1.0
 */
public interface DutySimtServices {

    //获取值班人列表
    List<DutySimt> list() throws Exception;

    //更新值班人
    void update(DutySimt dutySimt, TokenModel tokenModel)throws Exception;

    //查询某天值班人员
   String selectByDay(DutySimt dutySimt)throws Exception;


}
