package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: Trainjoinlist
 * @Author: 王哲
 * @Description: 培训参加名单
 * @Date: 2020/1/7 10:53
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trainjoinlist")
public class Trainjoinlist extends BaseModel {

    /**
     * 培训参加名单主键
     */
    @Id
    private String trainjoinlistid;

    /**
     * 开班培训列表主键
     */
    private String startprogramid;

    /**
     * 人员id
     */
    private String personnelid;

    /**
     * 部门名称
     */
    private String departmentname;

    /**
     * 部门id
     */
    private String Departmentid;

    /**
     * 根节点部门id
     */
    private String rootdepid;

    /**
     * 姓名
     */
    private String customername;

    /**
     * 员工号
     */
    private String jobnumber;

    /**
     * 身份证件号
     */
    private String idnumber;

    /**
     * 参加状态
     */
    private String jointype;

    /**
     * 成绩
     */
    private String performance;

    /**
     * 通过状态
     */
    private String throughtype;

    /**
     * 考试次数
     */
    private Integer number;


    /**
     * 最后考核日期
     */
    private Date finallyexamdate;

    /**
     * 备注
     */
    private String remark;
}
