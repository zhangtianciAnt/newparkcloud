<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.nt.service_AOCHUAN.AOCHUAN6000.mapper.ReimbursementDetailMapper">

    <!--  存在Check  -->
    <select id="existCheck" resultType="com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail">
        select * from reimbursementdetail where REIMBURSEMENT_DETAIL_ID = #{id} AND  STATUS = #{status}
    </select>

    <!--  获取记录表数据  -->
    <select id="getReimbursementDetailList" resultType="com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail">
        select *
        from reimbursementdetail
        where REIMBURSEMENT_NO =#{remd_no} AND STATUS = #{status}
	</select>

    <!--  记录表删除  -->
    <update id="deleteFromReimbursementDetailByPrikey">
         UPDATE reimbursementdetail
		SET STATUS = #{status},
		MODIFYBY = #{modifyby},
		MODIFYON = now()
		 WHERE REIMBURSEMENT_DETAIL_ID = #{id}
		 AND REIMBURSEMENT_NO = #{remd_no}
    </update>
</mapper>
