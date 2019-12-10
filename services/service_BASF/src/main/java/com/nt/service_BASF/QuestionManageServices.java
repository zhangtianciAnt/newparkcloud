package com.nt.service_BASF;

import com.nt.dao_BASF.QuestionManage;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: QuestionManageServices
 * @Author: Wxz
 * @Description: QuestionManageServices
 * @Date: 2019/11/20 18:34
 * @Version: 1.0
 */
public interface QuestionManageServices {

    //获取报警单列表
    List<QuestionManage> list() throws Exception;

    //创建报警单
    String insert(QuestionManage questionManage, TokenModel tokenModel) throws Exception;

    //删除报警单
    void delete(QuestionManage questionManage) throws Exception;

    //获取报警单详情
    QuestionManage one(String questionid) throws Exception;

    //更新报警单
    void update(QuestionManage questionManage, TokenModel tokenModel) throws Exception;

    //execl导入
    List<String> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception;
}
