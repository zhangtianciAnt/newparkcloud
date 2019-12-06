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
@Table(name = "retire")
public class Retire extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "RETIRE_ID")
    private String retire_id;

    @Column(name = "GIVING_ID")
    private String giving_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "RETIREDATE")
    private Date retiredate;

    @Column(name = "ATTENDANCE")
    private String attendance;

    @Column(name = "GIVE")
    private String give;

    @Column(name = "LUNCH")
    private String lunch;

    @Column(name = "TRAFFIC")
    private String traffic;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "JOBNUMBER")
    private String jobnumber;

    @Column(name = "ROWINDEX")
    private Integer rowindex;


}
