package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Vo.restViewVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    //年假申请check
    List<AbNormal> selectfinishAnnuel1(@Param("userid") String userid,@Param("year") String year);

    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认
    List<AbNormal> selectAbnomalBystatusandUserid(@Param("userid") String userid);
    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认

    //add ccm 0806 查询申请人的剩余年休，
    @Select("select * from annulleaveview where user_id = #{userid}")
    List<AnnualLeave> getremainingByuserid(@Param("userid") String userid);
    //add ccm 0806 查询申请人的剩余年休，

}
