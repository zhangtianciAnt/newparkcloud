package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleinformationVo extends BaseModel {

    /**
     * 出入车辆数
     */
    private Integer cnt;

    /**
     * 入场时间
     */
    private String date;

}
