package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Startprogram;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: StartprogramVo
 * @Author: 王哲
 * @Description: 培训列表Vo
 * @Date: 2020/1/8 17:45
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartprogramVo {

    /**
     * 申请人数
     */
    private Integer joinnumber;

    /**
     * 实际参加人数
     */
    private Integer actualjoinnumber;

    /**
     * 考核情况
     */


    /**
     * 培训列表
     */
    private Startprogram startprogram;

}
