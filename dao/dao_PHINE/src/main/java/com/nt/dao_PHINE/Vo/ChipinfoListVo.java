package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: ChipinfoListVo
 * @Description: 芯片列表Vo
 * @Author: MYT
 * @CreateDate: 2020/2/4
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChipinfoListVo {

    /**
     * ID
     */
    private String id;

    /**
     * 添加时指定，不可重复
     */
    private String chipid;

    /**
     * VU440、KU115等
     */
    private String chiptype;

    /**
     * 所属板卡编号
     */
    private String boardid;

    /**
     * 所在板卡槽位
     */
    private String chipslotid;

    /**
     * 备注
     */
    private String remarks;

}
