package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: DutyVo
 * @Author: Newtouch
 * @Description: 值班信息Vo
 * @Date: 2020/1/8 15:04
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DutyVo {

    //值班人
    private String duty;
    //备勤人
    private String backup;
    //值班其他
    private String dutyother;
    //备勤其他
    private String backupother;

}
