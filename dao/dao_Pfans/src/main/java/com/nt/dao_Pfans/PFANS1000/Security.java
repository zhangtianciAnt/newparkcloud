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
@Table(name = "security")
public class Security extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "SECURITY_ID")
    private String securityid;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "TEAM_ID")
    private String team_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SUBTYPE")
    private String subtype;

    @Column(name = "APPLICATION")
    private Date application;

    @Column(name = "EXTENSION")
    private String extension;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "REASON")
    private String reason;

}
