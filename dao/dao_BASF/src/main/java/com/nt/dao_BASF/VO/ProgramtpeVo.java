package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.QuestionManage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: ProgramtpeVo
 * @Author: 王哲
 * @Description: 培训题库获取Vo
 * @Date: 2020/3/3 9:56
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramtpeVo {
    /*
     *考试题目
     */
    Set<QuestionManage> questionManageSet;
    /**
     * 合格判定标准（%）
     */
    private Integer standard;


}
