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
 * @ClassName: Disciplinary
 * @Description: 月度賞与Model
 * @Author: SKAIXX
 * @CreateDate: 2020/3/18
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "disciplinary")
public class Disciplinary extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "DISCIPLINARY_ID")
    private String disciplinary_id;

    /**
     * 所属给与计算
     */
    @Column(name = "GIVING_ID")
    private String giving_id;

    /**
     * 名前
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 1月専項
     */
    @Column(name = "MONTH1")
    private String month1;

    /**
     * 2月専項
     */
    @Column(name = "MONTH2")
    private String month2;

    /**
     * 3月専項
     */
    @Column(name = "MONTH3")
    private String month3;

    /**
     * 4月専項
     */
    @Column(name = "MONTH4")
    private String month4;

    /**
     * 5月専項
     */
    @Column(name = "MONTH5")
    private String month5;

    /**
     * 6月専項
     */
    @Column(name = "MONTH6")
    private String month6;

    /**
     * 7月専項
     */
    @Column(name = "MONTH7")
    private String month7;

    /**
     * 8月専項
     */
    @Column(name = "MONTH8")
    private String month8;

    /**
     * 9月専項
     */
    @Column(name = "MONTH9")
    private String month9;

    /**
     * 10月専項
     */
    @Column(name = "MONTH10")
    private String month10;

    /**
     * 11月専項
     */
    @Column(name = "MONTH11")
    private String month11;

    /**
     * 12月専項
     */
    @Column(name = "MONTH12")
    private String month12;

    /**
     * 累計年間控除
     */
    @Column(name = "TOTAL")
    private String total;

    /**
     * 工号
     */
    @Column(name = "JOBNUMBER")
    private String jobnumber;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
