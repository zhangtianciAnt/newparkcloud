package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "replacerest")
public class Replacerest extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 代休记录
     */
    @Id
    @Column(name = "REPLACEREST_ID")
    private String replacerest_id;

    /**
     * 申请人
     */
    @Column(name = "USER_ID")
    private String user_id;

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
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private String application_date;

    /**
     * 代休类型
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 时长
     */
    @Column(name = "DURATION")
    private String duration;

    /**
     * 承认状态
     */
    @Column(name = "RECOGNITIONSTATE")
    private String recognitionstate;

}
