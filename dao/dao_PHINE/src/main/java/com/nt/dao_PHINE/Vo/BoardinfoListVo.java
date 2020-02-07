package com.nt.dao_PHINE.Vo;

import com.nt.dao_PHINE.Chipinfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: BoardinfoListVo
 * @Description: 板卡信息Vo
 * @Author: SKAIXX
 * @CreateDate: 2020/2/7
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardinfoListVo {

    /**
     * 板卡id
     */
    private String id;

    /**
     * 板卡编号
     */
    private String boardid;

    /**
     * 板卡类型
     */
    private String boardtype;

    /**
     * 板卡IP地址
     */
    private String boardipaddress;

    /**
     * 板卡内的芯片列表
     */
    private List<Chipinfo> chipinfoList;
}
