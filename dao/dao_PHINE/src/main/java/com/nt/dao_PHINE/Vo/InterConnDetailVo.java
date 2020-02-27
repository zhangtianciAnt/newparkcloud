package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: InterConnDetailVo
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/26
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterConnDetailVo {

    private String testitem;

    private String scrfpga;

    private String srcfmc;

    private String srcindex;

    private String dstfpga;

    private String dstfmc;

    private String dstindex;

    private String status;
}
