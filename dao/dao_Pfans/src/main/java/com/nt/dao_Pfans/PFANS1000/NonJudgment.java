package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.SplittableRandom;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="nonjudgment")
public class NonJudgment extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NONJUDGMENT_ID")
    private String nonjudgment_id;

    @Column(name = "DECISIONNUMBER")
    private String  decisionnumber;

    @Column(name = "CAREER")
    private String career;

    @Column(name = "DEPLOYMENT")
    private String deployment;

    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    @Column(name = "JANAME")
    private String janame;

    @Column(name = "CUSTOENGLISH")
    private String custoenglish;

    @Column(name = "VARTO")
    private String varto;

    @Column(name = "REVIEW")
    private String review;

    @Column(name = "REQUIREMENTS")
    private String requirements;

    @Column(name = "CLAIMDATETIME")
    private String claimdatetime;

    @Column(name = "TECHNICAL")
    private String technical;

    @Column(name = "POSSIBLE")
    private String possible;

    @Column(name = "WEAPON")
    private String weapon;

    @Column(name = "WEAPONRESON")
    private String weaponreson;

    @Column(name = "WORD")
    private String word;

    @Column(name = "WORDRESON")
    private String wordreson;

    @Column(name = "SPECIFI")
    private String specifi;

    @Column(name = "SPECIFIRESON")
    private String specifireson;

    @Column(name = "RESTRICTED")
    private String restricted;

    @Column(name = "REGULATION")
    private String regulation;

    @Column(name = "TODAY")
    private Date today;

    @Column(name = "EXPORT")
    private String export;

    @Column(name = "OUTNUMBER")
    private String  outnumber;

    @Column(name = "PRODUCTNUMBER")
    private String  productnumber;

    @Column(name = "OUTPUTLIMIT2")
    private String  outputlimit2;

    @Column(name = "MAREASON")
    private String  mareason;

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

    @Column(name = "SUPPLIESOF")
    private String suppliesof;

    @Column(name = "SUJUDEGRESULT")
    private String sujudegresult;

    @Column(name = "LIJUDEGRESULT")
    private String lijudegresult;

    @Column(name = "JXJUDGENO")
    private String jxjudgeno;

    @Column(name = "GFJUDGENO")
    private String gfjudgeno;

    @Column(name = "LYJUDGENO")
    private String lyjudgeno;

    @Column(name = "AMERICANTECHNO")
    private String americantechno;

    @Column(name = "OTHER1")
    private String other1;

    @Column(name = "OTHER2")
    private String other2;

    @Column(name = "OTHER3")
    private String other3;

    @Column(name = "OTHER4")
    private String other4;

    @Column(name = "OTHER5")
    private String other5;

    @Column(name = "OTHER6")
    private String other6;

    @Column(name = "OTHER7")
    private String other7;

    @Column(name = "OTHER8")
    private String other8;

    @Column(name = "OTHER9")
    private String other9;

    @Column(name = "OTHER10")
    private String other10;

    @Column(name = "OTHER11")
    private String other11;

    @Column(name = "OREASON")
    private String oreason;

    @Column(name = "OTHREASON")
    private String othreason;

    @Column(name = "OTHERREASON")
    private String otherreason;

    @Column(name = "ECCNJUDGENO")
    private String eccnjudgeno;
}
