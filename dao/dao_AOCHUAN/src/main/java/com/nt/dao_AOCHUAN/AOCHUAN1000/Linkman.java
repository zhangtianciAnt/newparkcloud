package com.nt.dao_AOCHUAN.AOCHUAN1000;

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
@Table(name = "linkman")
public class Linkman extends BaseModel {
    @Id
    @Column(name = "LINKMAN_ID")
    private String linkman_id;

    @Column(name = "BASEINFOR_ID")
    private String baseinfor_id;

//    中文名
    @Column(name = "CNAME")
    private String cname;

//    日文名
    @Column(name = "JNAME")
    private String jname;

//    韩文名
    @Column(name = "KNAME")
    private String kname;

//    英文名
    @Column(name = "ENAME")
    private String ename;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "FIXEDTELEPHONE")
    private String fixedtelephone;

    @Column(name = "MOBILEPHONE")
    private String mobilephone;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "FAX")
    private String fax;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "INTERNALPHONE")
    private String internalphone;

    @Column(name = "HEAD")
    private String head;

    @Column(name = "BIRTHDAY")
    private String birthday;

    @Column(name = "PERSONALCHARACTER")
    private String personalcharacter;

    @Column(name = "FAMILYSTATUS")
    private String familystatus;

    @Column(name = "FAMILYADDRESS")
    private String familyaddress;
}
