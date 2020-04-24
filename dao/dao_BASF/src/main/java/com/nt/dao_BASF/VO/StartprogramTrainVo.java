package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: StartprogramTrainVo
 * @Author: 王哲
 * @Description: 强制/非强制率用Vo
 * @Date: 2020/4/22 11:23
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartprogramTrainVo {

    /**
     * 部门名
     */
    private String department;

    /**
     * 通过率
     */
    private String throughtype;



}
