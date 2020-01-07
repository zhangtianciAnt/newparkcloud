package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FireAlarmStatisticsVo extends BaseModel {
    /**
     * 日期
     */
    private String date;

    /**
     * 当月每日接警数量
     */
    private Integer cnt;
}
