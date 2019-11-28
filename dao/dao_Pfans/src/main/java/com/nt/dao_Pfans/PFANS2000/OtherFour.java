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
@Table(name = "otherfour")
public class OtherFour extends BaseModel {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "OTHERFOUR_ID")
    private String otherfour_id;
    @Column(name = "DEPARTMENT_ID")
    private String department_id;
    @Column(name = "JOBNUMBER")
    private String jobnumber;
    @Column(name = "GIVING_ID")
    private String giving_id;
    @Column(name = "SOCIALSECURITY")
    private String socialsecurity;
    @Column(name = "USER_ID")
    private String user_id;
    @Column(name = "TOTAL")
    private String total;
    @Column(name = "REMARKS")
    private String remarks;
    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
