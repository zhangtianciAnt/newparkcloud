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
    List<Map<String,String>> queryCount(AbNormal abNormal);
    //endregion add scc 根据开始时间或实际开始时间判断一个月或一年的育儿假申请次数 to

    //region add scc 育儿假，根据主键判断当月是否申请其他育儿假 from
    List<AbNormal> find(AbNormal abNormal);
    //endregion add scc 根据主键判断当月是否申请其他育儿假 to

    //region scc add 判断父母照料假天数 from
    int queryDays(AbNormal abNormal);
    //region scc add 判断父母照料假天数 to
}
