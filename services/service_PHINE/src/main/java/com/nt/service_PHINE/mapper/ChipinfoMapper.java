package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Chipinfo;
import com.nt.dao_PHINE.Vo.ChipinfoListVo;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ChipinfoMapper extends MyMapper<Chipinfo> {

    // 项目创建画面获取芯片列表
    List<ChipinfoListVo> selectChipTypeList();
}
