package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Vo.restViewVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


public interface AbNormalMapper extends MyMapper<AbNormal> {
    List<AbNormal> selectAbNormal(@Param("now") String now);
    List<AbNormal> selectAbNormalThisYear(AbNormal abNormal);
    Double selectAbNormalDate(@Param("userid") String userid);
    List<AbNormal> selectAbNormalParent(@Param("userid") String userid);

    List<restViewVo> getRestday(@Param("userid") String userid);

    //当前年度已经审批通过的年休
    Double selectfinishAnnuel(@Param("userid") String userid,@Param("year") String year);

//    List<AbNormal> selectAbNormal1(@Param("") List<String>  );
}
