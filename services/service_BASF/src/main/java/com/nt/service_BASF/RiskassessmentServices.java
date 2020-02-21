package com.nt.service_BASF;

import com.nt.dao_BASF.Highriskarea;
import com.nt.dao_BASF.Riskassessment;
import com.nt.dao_BASF.VO.HighriskareaVo;
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

    //获取数据
    Riskassessment getData() throws Exception;

    //承诺公告更新
    void noticeUpdata(String notice, TokenModel tokenModel) throws Exception;

    List<Highriskarea> selecthig(TokenModel tokenModel,Highriskarea highriskarea) throws Exception;
    //insert
    void insert(TokenModel tokenModel, Highriskarea highriskarea) throws Exception;

    //update
    void update(TokenModel tokenModel, Highriskarea highriskarea) throws Exception;

    //delete
    void delete(TokenModel tokenModel, Highriskarea highriskarea) throws Exception;

}
