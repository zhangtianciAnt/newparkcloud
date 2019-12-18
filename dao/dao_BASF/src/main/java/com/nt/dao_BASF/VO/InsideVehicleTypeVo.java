package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsideVehicleTypeVo extends BaseModel {
    /**
     * 车辆类别
     */
    private String vehicletype;

    /**
     * 数量
     */
    private Integer cnt;
}
