package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: DutySimtVo
 * @Author: 王哲
 * @Description:值班SIMT Vo
 * @Date: 2020/1/8 14:49
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DutySimtVo {
    //值班
    private String duty;
    //备勤
    private String backup;
}
