package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.utils.MyMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface PersonalCostMapper extends MyMapper<PersonalCost>{

    List<PersonalCost> selectPersonalCost(@Param("groupid") String groupid,@Param("yearsantid") String yearsantid);

    //部门
    @Select("select GROUPID from personalcost where GROUPID is not null and yearsantid = #{yearsantid} group by GROUPID")
    List<String> getGroupId(@Param("yearsantid") String yearsantid);

    @Select("select DEPARTSHORT AS departshortBmSum,count(userid) AS peopleBmSum,sum(round(MONTHLYSALARY,2)) AS monthBmSum,sum(round(BASICALLYANT,2)) AS basicBmSum,sum(round(RESPONSIBILITYANT,2)) AS balityBmSum,sum(round(TOTALSUBSIDIES,2)) AS totalsubsidiesBmSum,sum(round(MONTHLYBONUS,2)) AS monthlybonusBmSum,sum(round(ANNUALBONUS,2)) AS annualbonusBmSum,sum(round(TOTALWAGES,2)) AS totalwagesBmSum,sum(round(TRADEUNIONFUNDS,2)) AS tradeunionfundsBmSum,sum(round(SBGSAJ,2)) AS sbgsajBmSum,sum(round(GJJGSFDAJ,2)) AS gjjgsfdajBmSum,sum(round(APTOJU,2)) AS aptojuBmSum,sum(round(SBGSJM,2)) AS sbgsjmBmSum,sum(round(GJJGSFDJM,2)) AS gjjgsfdjmBmSum,sum(round(JUTOMA,2)) AS jutomaBmSum,sum(round(OVERTIMEPAY,2)) AS overtimepayBmSum from personalcost where GROUPID = #{groupid} and yearsantid = #{yearsantid}")
    PersonalCostBmSum getPersonalCostBmSum(@Param("groupid") String groupid,@Param("yearsantid") String yearsantid);

    @Select("select LTRANK from personalcost where LTRANK is not null and GROUPID = #{groupid} and yearsantid = #{yearsantid} GROUP BY LTRANK order by LTRANK")
    List<String> getGroupinRanks(@Param("groupid") String groupid,@Param("yearsantid") String yearsantid);

    @Select("SELECT DEPARTSHORT AS departshortBmSum,LTRANK AS exrankBmSum,count( userid ) AS peopleBmSum,sum( round( MONTHLYSALARY, 2 ) ) AS monthBmSum,sum( round( BASICALLYANT, 2 ) ) AS basicBmSum,sum( round( RESPONSIBILITYANT, 2 ) ) AS balityBmSum,sum( round( TOTALSUBSIDIES, 2 ) ) AS totalsubsidiesBmSum,sum( round( MONTHLYBONUS, 2 ) ) AS monthlybonusBmSum,sum( round( ANNUALBONUS, 2 ) ) AS annualbonusBmSum,sum( round( TOTALWAGES, 2 ) ) AS totalwagesBmSum,sum( round( TRADEUNIONFUNDS, 2 ) ) AS tradeunionfundsBmSum,sum( round( SBGSAJ, 2 ) ) AS sbgsajBmSum,sum( round( GJJGSFDAJ, 2 ) ) AS gjjgsfdajBmSum,sum( round( APTOJU, 2 ) ) AS aptojuBmSum,sum( round( SBGSJM, 2 ) ) AS sbgsjmBmSum,sum( round( GJJGSFDJM, 2 ) ) AS gjjgsfdjmBmSum,sum( round( JUTOMA, 2 ) ) AS jutomaBmSum,sum( round( OVERTIMEPAY, 2 ) ) AS overtimepayBmSum from personalcost where GROUPID = #{groupid} and LTRANK = #{rank} and yearsantid = #{yearsantid}")
    PersonalCostBmSum getPersonalCostSum(@Param("groupid") String groupid, @Param("rank") String rank,@Param("yearsantid") String yearsantid);

    //公司
    @Select("select count( userid ) AS peopleGsSum,sum( round( MONTHLYSALARY, 2 ) ) AS monthGsSum,sum( round( BASICALLYANT, 2 ) ) AS basicGsSum,sum( round( RESPONSIBILITYANT, 2 ) ) AS balityGsSum,sum( round( TOTALSUBSIDIES, 2 ) ) AS totalsubsidiesGsSum,sum( round( MONTHLYBONUS, 2 ) ) AS monthlybonusGsSum,sum( round( ANNUALBONUS, 2 ) ) AS annualbonusGsSum,sum( round( TOTALWAGES, 2 ) ) AS totalwagesGsSum,sum( round( TRADEUNIONFUNDS, 2 ) ) AS tradeunionfundsGsSum,sum( round( SBGSAJ, 2 ) ) AS sbgsajGsSum,sum( round( GJJGSFDAJ, 2 ) ) AS gjjgsfdajGsSum,sum( round( APTOJU, 2 ) ) AS aptojuGsSum,sum( round( SBGSJM, 2 ) ) AS sbgsjmGsSum,sum( round( GJJGSFDJM, 2 ) ) AS gjjgsfdjmGsSum,sum( round( JUTOMA, 2 ) ) AS jutomaGsSum,sum( round( OVERTIMEPAY, 2 ) ) AS overtimepayGsSum  from personalcost where yearsantid = #{yearsantid} ")
    PersonalCostGsSum getPersonalCostGsSum(@Param("yearsantid") String yearsantid);

    //rank
    @Select("select LTRANK from personalcost where LTRANK is not null and yearsantid = #{yearsantid} GROUP BY LTRANK order by LTRANK")
    List<String> getCominRanks(@Param("yearsantid") String yearsantid);


    @Select("SELECT LTRANK AS exrankGsSum,count( userid ) AS peopleGsSum,sum( round( MONTHLYSALARY, 2 ) ) AS monthGsSum,sum( round( BASICALLYANT, 2 ) ) AS basicGsSum,sum( round( RESPONSIBILITYANT, 2 ) ) AS balityGsSum,sum( round( TOTALSUBSIDIES, 2 ) ) AS totalsubsidiesGsSum,sum( round( MONTHLYBONUS, 2 ) ) AS monthlybonusGsSum,sum( round( ANNUALBONUS, 2 ) ) AS annualbonusGsSum,sum( round( TOTALWAGES, 2 ) ) AS totalwagesGsSum,sum( round( TRADEUNIONFUNDS, 2 ) ) AS tradeunionfundsGsSum,sum( round( SBGSAJ, 2 ) ) AS sbgsajGsSum,sum( round( GJJGSFDAJ, 2 ) ) AS gjjgsfdajGsSum,sum( round( APTOJU, 2 ) ) AS aptojuGsSum,sum( round( SBGSJM, 2 ) ) AS sbgsjmGsSum,sum( round( GJJGSFDJM, 2 ) ) AS gjjgsfdjmGsSum,sum( round( JUTOMA, 2 ) ) AS jutomaGsSum,sum( round( OVERTIMEPAY, 2 ) ) AS overtimepayGsSum from personalcost where LTRANK = #{rank} and yearsantid = #{yearsantid}")
    PersonalCostGsSum getPersonalCostGSum(@Param("rank") String rank, @Param("yearsantid") String yearsantid);

    @Select("select APTOJU,JUTOMA FROM personalcost where userid = #{userid} and yearsantid = #{yearsantid}")
    PersonalCost getPersonalCostDetail(@Param("yearsantid") String yearsantid, @Param("userid") String userid);





    List<PersonalCost> selectPersonalCostResult(@Param("list") List<CustomerInfo> customerInfoList,@Param("yearsantid") String yearsantid);


    void updatePersonalCost(@Param("uplist") List<PersonalCost> personalCostList, @Param("tokenModel") TokenModel tokenModel);

    List<String> selectBirthUserid(@Param("year") String year);

    //add-lyt-21/2/19-PSDCD_PFANS_20201123_XQ_017-start
    List<PersonalCost> getFuzzyQuery(@Param("yearsantid") String yearsantid,@Param("username") String username,@Param("allotmentAnt") String allotmentAnt,@Param("group_id") String group_id,@Param("rnAnt") String rnAnt);
    //add-lyt-21/2/19-PSDCD_PFANS_20201123_XQ_017-end
}
