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
@Table(name = "psdcd")
public class Psdcd extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *PSDCD ID申請ID
     */
    @Id
    @Column(name = "PSDCD_ID")
    private String psdcd_id;

    /**
     * 所属センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;


    /**
     * 申请人
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 申請種類
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * サブ種類
     */
    @Column(name = "SUBTYPE")
    private String subtype;

    /**
     * 申請日付
     */
    @Column(name = "APPLICATION")
    private Date application;

    /**
     * E－Mail
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 内缐
     */
    @Column(name = "EXTENSION")
    private String extension;

    /**
     * ID申請穜别
     */
    @Column(name = "IDTYPE")
    private String idtype;


}
