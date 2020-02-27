package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_BASF.QuestionManage;
import com.nt.service_BASF.QuestionManageServices;
import com.nt.service_BASF.mapper.QuestionManageMapper;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
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
     * @Method eximport
     * @Author 王哲
     * @Version 1.0
     * @Description 导入题库
     * @Return void
     * @Date 2020/02/27 16:51
     */
    @Override
    public List<Object> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        //存放所有数据
        List<Object> allData = new ArrayList<>();
        //存放消息
        List<String> result = new ArrayList<>();
        //临时文件
        File f = null;
        //插入成功数
        int successCount = 0;
        //插入失败数
        int errorCount = 0;
        //excel集合
        List<List<Object>> list;
        //转换后的excel集合
        List<List<String>> strList = new ArrayList<>();
        //当前执行行数
        int k = 1;
        //存放数据
        List<QuestionManage> listVo = new ArrayList<QuestionManage>();
        try {
            //转化为文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取并存储为临时文件时发生未知异常！");
            allData.add(result);
            allData.add(listVo);
            return allData;
        }
        try {
            //读取excel临时文件
            ExcelReader reader = ExcelUtil.getReader(f);
            //读取到的excel集合
            list = reader.read();
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取Excel临时文件时发生未知异常！");
            allData.add(result);
            allData.add(listVo);
            return allData;
        }
        try {
            //判断是否是空文件
            if (list == null) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("空文件，请上传正确的文件");
                allData.add(result);
                allData.add(listVo);
                return allData;
            }
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("文件格式不正确！");
            allData.add(result);
            allData.add(listVo);
            return allData;
        }
        try {
            //判断行数
            if (list.size() < 1) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("空文件，请上传正确的文件");
                allData.add(result);
                allData.add(listVo);
                return allData;
            }
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("文件格式不正确！");
            allData.add(result);
            allData.add(listVo);
            return allData;
        }
        try {
            //转换为sing的list集合
            for (List<Object> list1 : list) {
                List<String> stringList = new ArrayList<>();
                for (Object obj : list1) {
                    stringList.add(obj.toString());
                }
                strList.add(stringList);
            }
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在数据获取时发生未知异常！");
            allData.add(result);
            allData.add(listVo);
            return allData;
        }
        try {
            //列名
            List<String> model = new ArrayList<String>();
            model.add("题型");
            model.add("题目");
            model.add("选项1");
            model.add("选项2");
            model.add("选项3");
            model.add("选项4");
            model.add("选项5");
            model.add("正确答案");
            model.add("答案解析");
            model.add("分值");
            //获取表第一列
            List<String> key = strList.get(0);
            //验证列名是否正确
            for (int i = 0; i < model.size(); i++) {
                if (!key.get(i).trim().equals(model.get(i))) {
                    result.add("第" + (i + 1) + "列标题错误，应为" + model.get(i) + "导入失败");
                    errorCount += 1;
                }
            }
            if (errorCount > 0) {
                result.add("失败数：" + errorCount);
                allData.add(result);
                allData.add(listVo);
                return allData;
            }
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在验证列标题时发生未知异常！");
            allData.add(result);
            allData.add(listVo);
            return allData;
        }
        try {
            for (int i = 1; i < strList.size(); i++) {
                k += 1;
                try {
                    QuestionManage questionManage = new QuestionManage();
                    //题型
                    if (StringUtils.isNotBlank(strList.get(i).get(0).trim())) {
                        switch (strList.get(i).get(0).trim()) {
                            case "单选题":
                                questionManage.setQuestiontype("BC036001");
                                break;
                            case "多选题":
                                questionManage.setQuestiontype("BC036002");
                                break;
                            case "填空题":
                                questionManage.setQuestiontype("BC036003");
                                break;
                            case "判断题":
                                questionManage.setQuestiontype("BC036004");
                                break;
                            default:
                                errorCount += 1;
                                result.add("题库第" + k + "行题型错误！");
                                continue;
                        }
                    }
                    //题目
                    if (StringUtils.isNotBlank(strList.get(i).get(1).trim())) {
                        questionManage.setQuestiontopic(strList.get(i).get(1).trim());
                    } else {
                        errorCount += 1;
                        result.add("题库第" + k + "行题目为空！");
                        continue;
                    }
                    //题目选项
                    if (
                            StringUtils.isBlank(strList.get(i).get(2)) &&
                                    StringUtils.isBlank(strList.get(i).get(3)) &&
                                    StringUtils.isBlank(strList.get(i).get(4)) &&
                                    StringUtils.isBlank(strList.get(i).get(5)) &&
                                    StringUtils.isBlank(strList.get(i).get(6))
                    ) {
                        if (questionManage.getQuestiontype() != "BC036003") {
                            errorCount += 1;
                            result.add("题库第" + k + "行非填空题无选项！");
                            continue;
                        }
                    } else {
                        questionManage.setOption1(strList.get(i).get(2).trim());
                        questionManage.setOption2(strList.get(i).get(3).trim());
                        questionManage.setOption3(strList.get(i).get(4).trim());
                        questionManage.setOption4(strList.get(i).get(5).trim());
                        questionManage.setOption5(strList.get(i).get(6).trim());
                    }
                    //正确答案
                    if (StringUtils.isNotBlank(strList.get(i).get(7).trim())) {
                        questionManage.setQuestionanswers(strList.get(i).get(7).trim());
                    } else {
                        errorCount += 1;
                        result.add("题库第" + k + "行正确答案为空！");
                        continue;
                    }
                    //答案解析
                    if (StringUtils.isNotBlank(strList.get(i).get(8).trim())) {
                        questionManage.setAnswersanalysis(strList.get(i).get(8).trim());
                    }
                    //分值
                    if (StringUtils.isNotBlank(strList.get(i).get(9).trim())) {
                        questionManage.setScore(strList.get(i).get(9).trim());
                    } else {
                        errorCount += 1;
                        result.add("题库第" + k + "行分值为空！");
                        continue;
                    }
                    listVo.add(questionManage);
                    successCount += 1;

                } catch (Exception e) {
                    errorCount += 1;
                    result.add("题库第" + k + "行读取时发生未知异常！");
                }
            }
            result.add("失败数：" + errorCount);
            result.add("成功数：" + successCount);
            allData.add(result);
            allData.add(listVo);
            return allData;
        } catch (Exception e) {
            result.add("系统在遍历题目数据时发生未知异常！");
            allData.add(result);
            allData.add(listVo);
            return allData;
        }
    }

}
