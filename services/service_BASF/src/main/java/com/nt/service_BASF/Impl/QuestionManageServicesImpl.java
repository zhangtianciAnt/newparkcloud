package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.QuestionManage;
import com.nt.service_BASF.QuestionManageServices;
import com.nt.service_BASF.mapper.QuestionManageMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: QuestionManageServicesImpl
 * @Author: Wxz
 * @Description: QuestionManageServicesImpl
 * @Date: 2019/11/20 18:36
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionManageServicesImpl implements QuestionManageServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private QuestionManageMapper questionManageMapper;

    /**
     * @param questionmanage
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取题库列表
     * @Return java.util.List<QuestionManage>
     * @Date 2019/11/20 18:40
     */
    @Override
    public List<QuestionManage> list() throws Exception {
        QuestionManage questionManage = new QuestionManage();
        return questionManageMapper.select(questionManage);
    }

    /**
     * @param questionManage
     * @param tokenModel
     * @Method insert
     * @Author Wxz
     * @Version 1.0
     * @Description 创建试题
     * @Return String
     * @Date 2019/11/20 18：42
     */
    @Override
    public String insert(QuestionManage questionManage, TokenModel tokenModel) throws Exception {
        questionManage.preInsert(tokenModel);
        String ccid = UUID.randomUUID().toString();
        questionManage.setQuestionid(ccid);
        questionManageMapper.insert(questionManage);
        return ccid;
    }

    /**
     * @param questionManage
     * @Method Delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除试题
     * @Return void
     * @Date 2019/11/20 18:47
     */
    @Override
    public void delete(QuestionManage questionManage) throws Exception {
        //逻辑删除（status -> "1"）
        questionManageMapper.updateByPrimaryKeySelective(questionManage);
    }

    /**
     * @param questionid
     * @Method one
     * @Author Wxz
     * @Version 1.0
     * @Description 获取题库详情
     * @Return com.nt.dao_BASF.QuestionManage
     * @Date 2019/11/20 18:49
     */
    @Override
    public QuestionManage one(String questionid) throws Exception {
        return questionManageMapper.selectByPrimaryKey(questionid);
    }

    /**
     * @param questionManage
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新题库详情
     * @Return void
     * @Date 2019/11/20 18:50
     */
    @Override
    public void update(QuestionManage questionManage, TokenModel tokenModel) throws Exception {
        questionManage.preUpdate(tokenModel);
        questionManageMapper.updateByPrimaryKey(questionManage);
    }
}
