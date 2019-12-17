package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_BASF.QuestionManage;
import com.nt.service_BASF.QuestionManageServices;
import com.nt.service_BASF.mapper.QuestionManageMapper;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
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

    @Autowired
    private DictionaryMapper dictionaryMapper;
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

    /**
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 导入导出题库
     * @Return void
     * @Date 2019/12/9 10:21
     */
    @Override
    public List<String> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<QuestionManage> listVo = new ArrayList<QuestionManage>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("题型");
            model.add("题目");
            model.add("选项一");
            model.add("选项二");
            model.add("选项三");
            model.add("选项四");
            model.add("选项五");
            model.add("分值");
            model.add("正确答案");
            model.add("答案解析");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                QuestionManage questionManage = new QuestionManage();
                List<Object> value = list.get(k);
                k++;
                Dictionary dictionary = new Dictionary();
                dictionary.setValue1(value.get(0).toString());
                List<Dictionary> dictionaryList = dictionaryMapper.select(dictionary);
                for (Dictionary dic : dictionaryList) {
                    if (dictionary != null) {
                        questionManage.setQuestiontype(dic.getCode());
                    }
                    if (dictionary == null) {
                        error = error + 1;
                        Result.add("模板第 + (k - 1) + 行的题型没有找到，请输入正确的题型，导入失败");
                    }
                }
                String a = "";
                if (value.get(2) == null) {
                    questionManage.setOption1(a);
                } else {
                    questionManage.setOption1(value.get(2).toString());
                }
                if (value.get(3) == null) {
                    questionManage.setOption2(a);
                } else {
                    questionManage.setOption2(value.get(3).toString());
                }
                if (value.get(4) == null) {
                    questionManage.setOption3(a);
                } else {
                    questionManage.setOption3(value.get(4).toString());
                }
                if (value.get(5) == null) {
                    questionManage.setOption4(a);
                } else {
                    questionManage.setOption4(value.get(5).toString());
                }
                if (value.get(6) == null) {
                    questionManage.setOption5(a);
                } else {
                    questionManage.setOption5(value.get(6).toString());
                }
                if (value != null && !value.isEmpty()) {
                    questionManage.setQuestiontopic(value.get(1).toString());
                    questionManage.setScore(value.get(7).toString());
                    questionManage.setQuestionanswers(value.get(8).toString());
                    questionManage.setAnswersanalysis(value.get(9).toString());
                }
                questionManage.preInsert(tokenModel);
                questionManage.setQuestionid(UUID.randomUUID().toString());
                questionManageMapper.insert(questionManage);
                listVo.add(questionManage);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
