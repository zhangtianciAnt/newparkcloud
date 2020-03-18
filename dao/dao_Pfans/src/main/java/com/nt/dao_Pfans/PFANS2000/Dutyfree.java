package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Pfans.PFANS2000
 * @ClassName: Dutyfree
 * @Description: 免税Model
 * @Author: SKAIXX
 * @CreateDate: 2020/3/18
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dutyfree")
public class Dutyfree extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "DUTYFREE_ID")
    private String dutyfree_id;

    /**
     * 名字
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 所属给与计算
     */
    @Column(name = "GIVING_ID")
    private String giving_id;

    /**
     * 1月税金
     */
    @Column(name = "TAXESMONTH1")
    private String taxesmonth1;

    /**
     * 2月税金
     */
    @Column(name = "TAXESMONTH2")
    private String taxesmonth2;

    /**
     * 3月税金
     */
    @Column(name = "TAXESMONTH3")
    private String taxesmonth3;

    /**
     * 4月税金
     */
    @Column(name = "TAXESMONTH4")
    private String taxesmonth4;

    /**
     * 5月税金
     */
    @Column(name = "TAXESMONTH5")
    private String taxesmonth5;

    /**
     * 6月税金
     */
    @Column(name = "TAXESMONTH6")
    private String taxesmonth6;

    /**
     * 7月税金
     */
    @Column(name = "TAXESMONTH7")
    private String taxesmonth7;

    /**
     * 8月税金
     */
    @Column(name = "TAXESMONTH8")
    private String taxesmonth8;

    /**
     * 9月税金
     */
    @Column(name = "TAXESMONTH9")
    private String taxesmonth9;

    /**
     * 10月税金
     */
    @Column(name = "TAXESMONTH10")
    private String taxesmonth10;

    /**
     * 11月税金
     */
    @Column(name = "TAXESMONTH11")
    private String taxesmonth11;

    /**
     * 12月税金
     */
    @Column(name = "TAXESMONTH12")
    private String taxesmonth12;

    /**
     * 各月免税收入额
     */
    @Column(name = "AMOUNT")
    private String amount;

    /**
     * 累計年間控除
     */
    @Column(name = "CUMULATIVE")
    private String cumulative;
}
