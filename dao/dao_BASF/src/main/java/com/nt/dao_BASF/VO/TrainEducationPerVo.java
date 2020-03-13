package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.tracking.TrackerBoosting;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainEducationPerVo {

    //true 存在唯一数据 false存在重复值
    private boolean state;

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

    //培训总时长
    private Double thelengthSum;

    //存储培训信息
    private List<TrainEducationPerVo2> startprograms;


}

