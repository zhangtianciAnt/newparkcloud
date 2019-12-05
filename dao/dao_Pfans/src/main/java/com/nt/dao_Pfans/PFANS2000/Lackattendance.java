package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "lackattendance")
public class Lackattendance extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "LACKATTENDANCE_ID")
    private String lackattendance_id;

    @Column(name = "GIVING_ID")
    private String giving_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "LASTDILIGENCE")
    private String lastdiligence;

    @Column(name = "LASTSHORTDEFICIENCY")
    private String lastshortdeficiency;

    @Column(name = "LASTCHRONICDEFICIENCY")
    private String lastchronicdeficiency;

    @Column(name = "LASTTOTAL")
    private String lasttotal;

    @Column(name = "THISDILIGENCE")
    private String thisdiligence;

    @Column(name = "THISSHORTDEFICIENCY")
    private String thisshortdeficiency;

    @Column(name = "THISCHRONICDEFICIENCY")
    private String thischronicdeficiency;

    @Column(name = "THISTOTAL")
    private String thistotal;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "GIVE")
    private String give;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

    @Column(name = "JOBNUMBER")
    private String jobnumber;
}
