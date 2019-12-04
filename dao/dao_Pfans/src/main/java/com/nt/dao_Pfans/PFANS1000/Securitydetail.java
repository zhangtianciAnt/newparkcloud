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
@Table(name = "securitydetail")
public class Securitydetail extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "SECURITYDETAIL_ID")
    private String securitydetailid;

    @Id
    @Column(name = "SECURITY_ID")
    private String securityid;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DETAILCENTER_ID")
    private String detailcenter_id;

    @Column(name = "DETAILGROUP_ID")
    private String detailgroup_id;

    @Column(name = "DETAILTEAM_ID")
    private String detailteam_id;

    @Column(name = "PHONENUMBER")
    private String phonenumber;

    @Column(name = "EMAILDETAIL")
    private String emaildetail;

    @Column(name = "STARTDATE")
    private Date startdate;

    @Column(name = "FABUILDING")
    private String fabuilding;

    @Column(name = "FBBUILDING")
    private String fbbuilding;

    @Column(name = "ENTRYMANAGER")
    private String entrymanager;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
