<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.nt.service_AOCHUAN.AOCHUAN6000.mapper.ReimbursementMapper">

    <!--  存在Check  -->
    <select id="existCheck" resultType="com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement">
        SELECT * FROM reimbursement WHERE REIMBURSEMENT_ID = #{id} AND  STATUS = #{status}
    </select>

    <!--  唯一性Check  -->
    <select id="uniqueCheck" resultType="com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement">
        SELECT * FROM reimbursement WHERE REIMBURSEMENT_ID != #{id} AND REIMBURSEMENT_NO = #{reim_no}
    </select>

    <!--  项目表删除  -->
    <update id="deleteFromReimbursementByDoubleKey">
        UPDATE reimbursement
		SET STATUS = #{status},
		MODIFYBY = #{modifyby},
		MODIFYON = now()
		 WHERE REIMBURSEMENT_ID = #{id}
    </update>

</mapper>
