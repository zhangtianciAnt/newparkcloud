package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: TrainjoinlistEnhanceVo
 * @Author: Newtouch
 * @Description: 培训参加名单增强，获取更详细的信息
 * @Date: 2020/1/10 10:50
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainjoinlistEnhanceVo {

    /**
     * 培训参加名单主键
     */
    private String trainjoinlistid;

    /**
     * 开班培训列表id
     */
    private String startprogramid;

    /*
    人员id
    */
    private String personnelid;

    /*
    部门
    */
    private String departmentid;

    /*
    姓名
    */
    private String customername;

    /*
    员工号
    */
    private String userno;

    /*
    身份证件号
    */
    private String driveridnumber;

    /*
    参加状态
    */
    private String jointype;

    /*
    成绩
    */
    private String performance;

}
