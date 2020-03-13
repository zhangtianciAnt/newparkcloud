package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: TrainEducationPerVo2
 * @Author: 王哲
 * @Description: 培训教育查找人员培训信息（大屏用）
 * @Date: 2020/3/13 11:21
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainEducationPerVo2 {

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
