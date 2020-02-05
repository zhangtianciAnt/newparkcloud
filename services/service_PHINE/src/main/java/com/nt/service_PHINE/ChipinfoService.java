package com.nt.service_PHINE;

import com.nt.dao_PHINE.Vo.ChipinfoListVo;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: ChipinfoService
 * @Description: 芯片信息Service接口
 * @Author: MYT
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
public interface ChipinfoService {

    // 项目创建页面获取芯片列表
    List<ChipinfoListVo> getChipTypeList();
}
