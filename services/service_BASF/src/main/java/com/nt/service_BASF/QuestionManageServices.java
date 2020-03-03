package com.nt.service_BASF;

import com.nt.dao_BASF.QuestionManage;
import com.nt.dao_BASF.VO.ProgramtpeVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

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

    //获取题库列表
    List<QuestionManage> list() throws Exception;

    //创建试题
    String insert(QuestionManage questionManage, TokenModel tokenModel) throws Exception;

    //创建试题List
    void insertList(List<QuestionManage> questionManageList, TokenModel tokenModel) throws Exception;

    //删除试题
    void delete(QuestionManage questionManage) throws Exception;

    //根据培训计划id删除试题
    void deleteList(String programtpe) throws Exception;

    //获取试题详情
    QuestionManage one(String questionid) throws Exception;

    //更新试题
    void update(QuestionManage questionManage, TokenModel tokenModel) throws Exception;

    //execl导入
    List<Object> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception;

    //获取考试题目
    ProgramtpeVo getQuestions(String startprogramid) throws Exception;
}
