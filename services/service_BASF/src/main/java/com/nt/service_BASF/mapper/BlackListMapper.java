package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.BlackList;
import com.nt.dao_BASF.VO.BlackListVo;
import com.nt.utils.MyMapper;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: DriverInformationMapper
 * @Author: Wxz
 * @Description: DriverInformationMapper
 * @Date: 2019/11/22 15:04
 * @Version: 1.0
 */
public interface BlackListMapper extends MyMapper<BlackList> {
    List<BlackListVo> getBlackList() throws Exception;
}
