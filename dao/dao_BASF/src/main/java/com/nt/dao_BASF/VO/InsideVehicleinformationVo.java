package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsideVehicleinformationVo extends BaseModel {
    /**
     * 车牌号
     */
    private String vehiclenumber;

    /**
     * 运输公司
     */
    private String transporter;

    /**
     * 入场时间
     */
    private String intime;

    /**
     * 货物名称
     */
    private String goodsname;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 即时速度
     */
    private String speed;

    /**
     * 是否是危化品车辆
     */
    private String ishazardous;
}
