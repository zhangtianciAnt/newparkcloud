package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Vehicleinformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: VehicleinformationGpsArrVo
 * @Author: 王哲
 * @Description: 车辆信息表Vo（gis用）
 * @Date: 2020/4/22 14:21
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleinformationGpsArrVo extends Vehicleinformation {
    ArrayList gpsArr;
}
