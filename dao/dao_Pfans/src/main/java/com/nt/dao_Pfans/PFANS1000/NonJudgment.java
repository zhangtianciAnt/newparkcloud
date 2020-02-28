package com.nt.dao_Pfans.PFANS1000;

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
@Table(name="nonjudgment")
public class NonJudgment extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NONJUDGMENT_ID")
    private String nonjudgment_id;

    @Column(name = "NO")
    private String   no;

    @Column(name = "CAREER")
    private String career;

    @Column(name = "ORGANIZATION")
    private String organization;

    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    @Column(name = "JANAME")
    private String janame;

    @Column(name = "DEPOSITARY")
    private String depositary;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "REVIEW")
    private String review;

    @Column(name = "REQUIREMENTS")
    private String requirements;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "TECHNICAL")
    private String technical;

    @Column(name = "TODAY")
    private Date today;

    @Column(name = "EXPORT")
    private String export;

    @Column(name = "JX-JUDGENO")
    private String   jxjudgeno;

    @Column(name = "GF-JUDGENO")
    private String gfjudgeno;

    @Column(name = "LY-JUDGENo")
    private String  lyjudgeno;

    @Column(name = "MANAGEMENT2")
    private String  management2;

    @Column(name = "REASON")
    private String  reason;

    @Column(name = "MANAGEMENT3")
    private String management3;

    @Column(name = "MANAGEMENT4")
    private String management4;

    @Column(name = "JUDGED")
    private String judged;

    @Column(name = "AMERICANTECHNOLOGY")
    private String americantechnology;

    @Column(name = "JUDGECOMPLEMENT")
    private String judgecomplement;

    @Column(name = "EXPORTLICENSE")
    private String exportlicense;

    @Column(name = "DECRESULT")
    private String decresult;

    @Column(name = "LIMITCOMMUNT")
    private String limitcommunt;

    @Column(name = "LIMITDELIVERY")
    private String limitdelivery;

    @Column(name = "LIMITCOMPUTERS")
    private String limitcomputers;

    @Column(name = "LIMITJASOFTWARE")
    private String limitjasoftware;

    @Column(name = "LIMITTECHNOLOGY")
    private String limittechnology;

    @Column(name = "SUPPLIEINDUSTRIAL")
    private String supplieindustrial;

    @Column(name = "SUPPLIELECTRONIC")
    private String supplielectronic;

    @Column(name = "SUPPLIECOM")
    private String suppliecom;

}
