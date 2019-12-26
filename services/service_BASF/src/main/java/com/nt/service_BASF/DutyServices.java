package com.nt.service_BASF;

import com.nt.dao_BASF.Duty;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: DutyServices
 * @Author: 王哲
 * @Description: 值班信息表接口类
 * @Date: 2019/12/25 10:49
 * @Version: 1.0
 */
public interface DutyServices {

    //excel文档导入
    List<String> importexcel(HttpServletRequest request, TokenModel tokenModel) throws Exception;

    //当天值班人员查询
    Duty selectDayDuty() throws Exception;

}
