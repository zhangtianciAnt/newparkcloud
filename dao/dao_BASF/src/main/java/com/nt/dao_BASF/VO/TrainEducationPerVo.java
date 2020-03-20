package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainEducationPerVo {

    /**
     * 培训教育人员id
     */
    private String personnelid;

    /**
     * 姓名
     */
    private String customername;

    //员工号
    private String jobnumber;

    //卡号
    private String documentnumber;

    //部门
    private String departmentname;

    /**
     * 培训名称
     */
    private String programname;
    /**
     * 培训时间
     */
    private String starttheorydate;
    /**
     * 通过/未通过
     */
    private String throughtype;

    //培训时长
    private Double thelength;
}

