package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trialsoftdetail")
public class Trialsoftdetail extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * フリー・試用ソフト使用申請明细副表
     */
    @Column(name = "TRIALSOFTDETAIL_ID")
    private String trialsoftdetail_id;

    /**
     * フリー・試用ソフト使用申請
     */
    @Column(name = "TRIALSOFT_ID")
    private String trialsoft_id;

    /**
     * 機器名
     */
    @Column(name = "MACHINENAME")
    private String machinename;

    /**
     * 利用者
     */
    @Column(name = "CUSTOMER")
    private String customer;

    /**
     * 利用開始日
     */
    @Column(name = "STARTDATE")
    private Date startdate;

    /**
     * 利用終了日
     */
    @Column(name = "ENDDATE")
    private Date enddate;

    /**
     * ソフトウェア名
     */
    @Column(name = "SOFTWARENAME")
    private String softwarename;

    /**
     * 性質
     */
    @Column(name = "NATURE")
    private String nature;

    /**
     * 開発者
     */
    @Column(name = "DEVELOPER")
    private String developer;

    /**
     * 用途
     */
    @Column(name = "EMPLOY")
    private String employ;

    /**
     * ソフトタイプ
     */
    @Column(name = "SOFTTYPE")
    private String softtype;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
