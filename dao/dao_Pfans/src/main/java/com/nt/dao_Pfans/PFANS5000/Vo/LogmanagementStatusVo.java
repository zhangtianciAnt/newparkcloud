package com.nt.dao_Pfans.PFANS5000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogmanagementStatusVo {

    /**
     * 人员
     */
    private String createby;

    /**
     * 时间
     */
    private String logdate;

    /**
     * 工时
     */
    private String timestart;

}
