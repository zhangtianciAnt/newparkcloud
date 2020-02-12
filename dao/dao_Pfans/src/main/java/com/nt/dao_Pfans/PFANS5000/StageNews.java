package com.nt.dao_Pfans.PFANS5000;

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
@Table(name = "stagenews")
public class StageNews extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "STAGENEWS_ID")
    private String stagenews_id;

    @Column(name = "CLOSEAPPLICAT_ID")
    private String closeapplicatid;

    @Column(name = "WORKSTAGE")
    private String workstage;

    @Column(name = "STAGETHING")
    private String stagething;

    @Column(name = "PREDICTNUKMBER")
    private Date predictnukmber;

    @Column(name = "PROJECTNUMBER")
    private String projectnumber;

    @Column(name = "STDATETIME")
    private String stdatetime;

    @Column(name = "NOTE1")
    private String note1;

    @Column(name = "FINSHTIME")
    private String finshtime;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
