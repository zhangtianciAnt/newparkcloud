package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: TrainingRecordsExport
 * @Author: 王哲
 * @Description: 用于培训档案导出
 * @Date: 2020/4/17 15:33
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRecordsExportVo {

    //部门
    private String departmentname;

    /**
     * 培训教育人员id
     */
    private String personnelid;

    //培训名称
    private String programname;

    //培训发生时间（线下）
    private Date starttheorydate;

    /**
     * 培训发生时间（线上）
     */
    private Date finallyexamdate;

    //内部/外部
    private String insideoutside;

    //线上/线下
    private String isonline;

    //强制/非强制
    private String mandatory;

    //初训/复训
    private String isfirst;

    /**
     * 通过状态
     */
    private String throughtype;

    /**
     * 成绩
     */
    private String performance;

    /**
     * 考试次数
     */
    private Integer number;
}
