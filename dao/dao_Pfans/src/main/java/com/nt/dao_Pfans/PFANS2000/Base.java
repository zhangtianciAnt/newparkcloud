package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "base")
public class Base extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 基数ID
     */
    @Id
    @Column(name = "base_id")
    private String base_id;

    /**
     * 給与計算ID
     */
    @Column(name = "GIVING_ID")
    private String giving_id;

    /**
     * 部门
     */
    @Column(name = "department_id")
    private String department_id;

    /**
     * 名字
     */
    @Column(name = "user_id")
    private String user_id;

    /**
     * Rn
     */
    @Column(name = "rn")
    private String rn;

    /**
     * 性別
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 独生子女
     */
    @Column(name = "onlychild")
    private String onlychild;

    /**
     * 入/退職/産休
     */
    @Column(name = "type")
    private String type;

    /**
     * 奨金計上
     */
    @Column(name = "bonus")
    private String bonus;

    /**
     * 1999年前社会人
     */
    @Column(name = "sociology")
    private String sociology;

    /**
     * 大連戸籍
     */
    @Column(name = "registered")
    private String registered;

    /**
     * 2019年6月
     */
    @Column(name = "mastmonth")
    private String mastmonth;

    /**
     * 2019年7月
     */
    @Column(name = "thismonth")
    private String thismonth;

    /**
     * 養老・失業・工傷基数
     */
    @Column(name = "pension")
    private String pension;

    /**
     * 医療・生育基数
     */
    @Column(name = "medical")
    private String medical;

    /**
     * 公积金基数
     */
    @Column(name = "accumulation")
    private String accumulation;

    /**
     * 采暖费
     */
    @Column(name = "heating")
    private String heating;

    /**
     * 入社日
     */
    @Column(name = "workdate")
    private Date workdate;

    /**
     * 顺序
     */
    @Column(name = "rowindex")
    private Integer rowindex;
}
