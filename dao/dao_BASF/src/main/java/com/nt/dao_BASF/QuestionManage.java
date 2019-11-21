package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: QuestionManage
 * @Author: Wxz
 * @Description: QuestionManage
 * @Date: 2019/11/20 18:24
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questionmanage")
public class QuestionManage extends BaseModel {
    @Id
    private String questionid;

    /**
     * 题型
     */
    private String questiontype;

    /**
     * 题目
     */
    private String questiontopic;

    /**
     * 选项一
     */
    private String option1;

    /**
     * 选项二
     */
    private String option2;

    /**
     * 选项三
     */
    private String option3;

    /**
     * 选项四
     */
    private String option4;

    /**
     * 选项五
     */
    private String option5;

    /**
     * 分值
     */
    private String score;

    /**
     * 正确答案
     */
    private String questionanswers;

    /**
     * 答案解析
     */
    private String answersanalysis;

}
