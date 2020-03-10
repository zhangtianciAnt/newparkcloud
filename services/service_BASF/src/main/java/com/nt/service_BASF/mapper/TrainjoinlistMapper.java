package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Emergencytemplate;
import com.nt.dao_BASF.Trainjoinlist;
import com.nt.dao_BASF.VO.OverduePersonnelListVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: TrainjoinlistMapper
 * @Author: 王哲
 * @Description: 培训参加名单Mapper
 * @Date: 2020/1/7 11:10
 * @Version: 1.0
 */
@Component(value = "TrainjoinlistMapper")
public interface TrainjoinlistMapper extends MyMapper<Trainjoinlist> {

    List<OverduePersonnelListVo> OverduePersonnelList();

    //结果发布判断该培训是否存在人员通过状态为空
    int isNotThroughtype(@Param("startprogramid") String startprogramid) throws Exception;

}
