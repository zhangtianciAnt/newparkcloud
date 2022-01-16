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
import java.util.Map;


public interface AbNormalMapper extends MyMapper<AbNormal> {
    List<AbNormal> selectAbNormal(@Param("now") String now);

    List<AbNormal> selectAbNormalGiving(@Param("now") String now);
    Double selectAbNormalThisYear(@Param("user_id") String user_id,@Param("year") String year);
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

    //add ccm 0812 考情管理查看当天的异常申请数据
    List<AbNormal> getabnormalByuseridandDate(@Param("userid") String userid,@Param("dates") String dates);
    //add ccm 0812 考情管理查看当天的异常申请数据

    Double selectAttenSumSick(Attendance attendance);
    List<AbNormal> selectAttenSumSick1(Attendance attendance);
    List<AbNormal> selectAttenSumSick2(Attendance attendance);

    //region add scc 根据开始时间或实际开始时间判断一个月或一年的育儿假申请次数 from
    Double selectAttenSumParent(AbNormal abNormal);
    List<AbNormal> selectAttenSumParent1(AbNormal abNormal);
    List<AbNormal> selectAttenSumParent2(AbNormal abNormal);
    Double selectAttenSumParenting(AbNormal abNormal);
    List<AbNormal> selectAttenSumParenting1(AbNormal abNormal);
    List<AbNormal> selectAttenSumParenting2(AbNormal abNormal);
    //endregion add scc 根据开始时间或实际开始时间判断一个月或一年的育儿假申请次数 to

    //region scc add 查询一年前的数据，育儿假和父母照料假 from
    List<AbNormal> lookingFor(@Param("user_id") String user_id, @Param("errortype") String errortype, @Param("occurrencedate") String occurrencedate);
    //endregion scc add 查询一年前的数据，育儿假和父母照料假 to

}
