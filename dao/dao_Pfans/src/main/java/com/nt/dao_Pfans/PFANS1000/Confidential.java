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
@Table(name = "confidential")
public class Confidential extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "CONFIDENTIAL_ID")
    private String confidentialid;

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

    @Column(name = "APPLICATION")
    private Date application;

    @Column(name = "MACHINEMEDIA")
    private String machinemedia;

    @Column(name = "MANAGEMENT")
    private String management;

    @Column(name = "EXPORTDATE")
    private Date exportdate;

    @Column(name = "RETURNDATE")
    private Date returndate;

    @Column(name = "HOLDINGPLACE")
    private String holdingplace;

    @Column(name = "COMPATIBLESEAL")
    private String compatibleseal;

    @Column(name = "HOLDOUTREASON")
    private String holdoutreason;

    @Column(name = "COMPANY")
    private String company;

    @Column(name = "SECRET")
    private String secret;

    @Column(name = "INFORMATION")
    private String information;

    @Column(name = "INTELLIGENCE")
    private String intelligence;

    @Column(name = "CONFIDENT")
    private String confident;

    /**
     * 对应状态
     */
    @Column(name = "CORRESPONDING")
    private String corresponding;
}
