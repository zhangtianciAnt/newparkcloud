package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contractcompound")
public class Contractcompound extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 金额分配
     */
    @Id
    @Column(name = "CONTRACTCOMPOUND_ID")
    private String contractcompound_id;

    /**
     * 契約書番号
     */
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    /**
     * 請求方式
     */
    @Column(name = "CLAIMTYPE")
    private String claimtype;

    /**
     * GROUP
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 请求金额
     */
    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    /**
     * 分配金额
     */
    @Column(name = "CONTRACTREQUESTAMOUNT")
    private String contractrequestamount;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    @OrderBy("ASC")
    private Integer rowindex;

}
