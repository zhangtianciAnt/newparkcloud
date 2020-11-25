package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.xfeatures2d.DAISY;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "themeinfor")

public class ThemeInfor extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "THEMEINFOR_ID")
    private String themeinfor_id;


    @Column(name = "THEMENAME")
    private String themename;


    @Column(name = "CENTERID")
    private String centerid;


    @Column(name = "GROUPID")
    private String groupid;


    @Column(name = "YEAR")
    private String year;


    @Column(name = "USERID")
    private String user_id;


    @Column(name = "DATA")
    private Date data;


    @Column(name = "DIVIDE")
    private String divide;


    @Column(name = "OTHERONE")
    private String otherone;


    @Column(name = "OTHERTWO")
    private String othertwo;


    @Column(name = "OTHERTHREE")
    private String otherthree;


    @Column(name = "REMARK")
    private String remark;

    @Column(name = "CONTRACT")
    private String contract;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TOOLSORGS")
    private String toolsorgs;

    @Column(name = "SUPPLIERINFOR_ID")
    private String supplierinfor_id;

    @Column(name = "CUSTOMERINFOR_ID")
    private String customerinfor_id;
}
