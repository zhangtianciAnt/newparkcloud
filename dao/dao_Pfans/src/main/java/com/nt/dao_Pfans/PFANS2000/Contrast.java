package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contrast")
public class Contrast extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 個人対比
     */
    @Id
    @Column(name = "CONTRAST_ID")
    private String contrast_id;

    /**
     * 給与計算ID
     */
    @Column(name = "GIVING_ID")
    private String giving_id;

    /**
     * 部門
     */
    @Column(name = "DEPARTMENT_ID")
    private String department_id;

    /**
     * 名前
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 2019年7月①手取
     */
    @Column(name = "THISMONTH")
    private String thismonth;

    /**
     * 2019年6月②手取
     */
    @Column(name = "LASTMONTH")
    private String lastmonth;

    /**
     * 差額①-➁
     */
    @Column(name = "DIFFERENCE")
    private String difference;

    /**
     * 原因
     */
    @Column(name = "REASON")
    private String reason;
}
