package com.nt.dao_Pfans.PFANS6000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "variousfunds")
public class Variousfunds extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     *各种经费
     */
    @Id
    @Column(name = "VARIOUSFUNDS_ID")
    private String variousfunds_id;

    /**
     *PJ名
     */
    @Column(name = "PJNAME")
    private String pjname;

    /**
     *PSDCD窓口
     */
    @Column(name = "PSDCDWINDOW")
    private String psdcdwindow;

    /**
     *BP会社名
     */
    @Column(name = "BPCLUBNAME")
    private String bpclubname;

    /**
     *BP担当者
     */
    @Column(name = "BPPLAYER")
    private String bpplayer;

    /**
     *PL表支払予定月
     */
    @Column(name = "PLMONTHPLAN")
    private String plmonthplan;

    /**
     *経費種類
     */
    @Column(name = "TYPEOFFEES")
    private String typeoffees;

    /**
     *支払い金額￥
     */
    @Column(name = "PAYMENT")
    private String payment;

    /**
     *備考
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     *年度
     */
    @Column(name = "YEAR")
    private Date year;
    // GROUPID
    @Column(name = "GROUP_ID")
    private String groupid;

}
