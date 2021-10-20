package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: BlackListVo
 * @Author: Wxz
 * @Description: BlackListVo
 * @Date: 2019/11/22 14:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackListApiVo {

    /**
     * 违规类型
     */
    private String violationtype;

    /**
     *车牌号
     */
    private String vehiclenumber;

}
