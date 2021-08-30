package com.nt.dao_Pfans.PFANS4000;

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
@Table(name = "peoplewarefee")
public class PeoplewareFee extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 人件费ID
     * */
    @Id
    @Column(name = "PEOPLEWARE_ID")
    private String peoplewareid;

    /**
     * 部门ID
     * */
    @Column(name = "GROUP_ID")
    private String groupid;

    /**
     * 年度
     * */
    @Column(name = "YEAR")
    private String year;

    /**
     * 员工RANK
     * */
    @Column(name = "RANKS")
    private String ranks;


    @Column(name = "MONTH1")
    private String month1 ;

    @Column(name = "MONTH2")
    private String month2;

    @Column(name = "MONTH3")
    private String month3;

    @Column(name = "MONTH4")
    private String month4;

    @Column(name = "MONTH5")
    private String month5;

    @Column(name = "MONTH6")
    private String month6;

    @Column(name = "MONTH7")
    private String month7;

    @Column(name = "MONTH8")
    private String month8;

    @Column(name = "MONTH9")
    private String month9;

    @Column(name = "MONTH10")
    private String month10;

    @Column(name = "MONTH11")
    private String month11;

    @Column(name = "MONTH12")
    private String month12;

}
