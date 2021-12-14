package com.nt.dao_Pfans.PFANS5000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projectsystem")
public class Projectsystem extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 项目体制
     */
    @Id
    @Column(name = "PROJECTSYSTEM_ID")
    private String projectsystem_id;

    /**
     * 所属公司项目
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * 社内外协区分(0为社内1为外协2为构外)
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 编号
     */
    @Column(name = "NUMBER")
    private String number;

    /**
     * 协力公司,构外时为外注公司
     */
    @Column(name = "COMPANY")
    private String company;

    /**
     * 姓名
     */
    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_ID")
    private String name_id;
    /**
     * 外注人员ID
     */
    @Column(name = "SUPPLIERNAMEID")
    private String suppliernameid;

    /**
     * 职位
     */
    @Column(name = "POSITION")
    private String position;

    /**
     * 入场时间,构外时为开始时间
     */
    @Column(name = "ADMISSIONTIME")
    private Date admissiontime;

    /**
     * 退场时间,构外时为结束时间
     */
    @Column(name = "EXITTIME")
    private Date exittime;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;
    //region add_qhr_20210810 添加rank、报告者字段
    /**
     * RANK
     */
    @Column(name = "`RANK`")
    private String rank;

    /**
     * 报告者
     */
    @Column(name = "REPORTER")
    private String reporter;
    //endregion add_qhr_20210810 添加rank、报告者字段

    //region scc add 添加构外tab页字段
    /**
     * 人月数，构外
     */
    @Column(name = "NUMBEROFMONTHS")
    private String numberofmonths;

    /**
     * 每月管理规模，构外
     */
    @Column(name = "MONTHLYSCALE")
    private String monthlyscale;

    /**
     * 窗口，构外
     */
    @Column(name = "MADOGUCHI")
    private String madoguchi;

    /**
     * 构外记录对应合同号，构外
     */
    @Column(name = "CONTRACTNO")
    private String contractno;

    /**
     * 对应合同人月数，构外
     */
    @Column(name = "TOTALNUMBER")
    private String totalnumber;
    //endregion scc add 添加构外tab页字段

    //委托合同平均金额
    @Column(name = "AMOUNTOF")
    private String amountof;
}
