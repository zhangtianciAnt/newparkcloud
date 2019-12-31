package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleAccessStatisticsVo extends BaseModel {

    /**
     * 出入车辆数
     */
    private Integer cnt;

    /**
     * 统计日期
     */
    private String date;

}
