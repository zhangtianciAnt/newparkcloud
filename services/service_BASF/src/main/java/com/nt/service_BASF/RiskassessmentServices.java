package com.nt.service_BASF;

import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: RiskassessmentServices
 * @Author: 王哲
 * @Description: 风险判刑表接口类
 * @Date: 2020/02/04 10:35
 * @Version: 1.0
 */
public interface RiskassessmentServices {

    //excel文档导入
    List<String> importexcel(HttpServletRequest request, TokenModel tokenModel) throws Exception;

}
