package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "totalplan")
public class Totalplan extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 人員計画合計ID
     */
    @Id
    @Column(name = "TOTALPLAN_ID")
    private String totalplan_id;

    /**
     * 事业计划ID
     */
    @Column(name = "BUSINESSPLAN_ID")
    private String businessplanid;

    @Column(name = "NUMBER4")
    private String number4;

    @Column(name = "WORKINGHOURS4")
    private String workinghours4;

    @Column(name = "NUMBER5")
    private String number5;

    @Column(name = "WORKINGHOURS5")
    private String workinghours5;

    @Column(name = "NUMBER6")
    private String number6;

    @Column(name = "WORKINGHOURS6")
    private String workinghours6;

    @Column(name = "NUMBER7")
    private String number7;

    @Column(name = "WORKINGHOURS7")
    private String workinghours7;

    @Column(name = "NUMBER8")
    private String number8;

    @Column(name = "WORKINGHOURS8")
    private String workinghours8;

    @Column(name = "NUMBER9")
    private String number9;

    @Column(name = "WORKINGHOURS9")
    private String workinghours9;

    @Column(name = "NUMBER10")
    private String number10;

    @Column(name = "WORKINGHOURS11")
    private String workinghours11;

    @Column(name = "NUMBER12")
    private String number12;

    @Column(name = "WORKINGHOURS12")
    private String workinghours12;

    @Column(name = "NUMBER1")
    private String number1;

    @Column(name = "WORKINGHOURS1")
    private String workinghours1;

    @Column(name = "NUMBER2")
    private String number2;

    @Column(name = "WORKINGHOURS2")
    private String workinghours2;

    @Column(name = "NUMBER3")
    private String number3;

    @Column(name = "WORKINGHOURS3")
    private String workinghours3;

    @Column(name = "NUMBERFIRSTHALF")
    private String numberfirsthalf;

    @Column(name = "WORKINGHOURSFIRSTHALF")
    private String workinghoursfirsthalf;

    @Column(name = "NUMBERSECONDHALF")
    private String numbersecondhalf;

    @Column(name = "WORKINGHOURSSECONDHALF")
    private String workinghourssecondhalf;

    @Column(name = "NUMBERANNUAL")
    private String numberannual;

    @Column(name = "WORKINGHOURSANNUAL")
    private String workinghoursannual;

    /**
     * 排序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
