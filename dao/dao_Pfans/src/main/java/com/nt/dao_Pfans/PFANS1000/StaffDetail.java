package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "staffdetail")
public class StaffDetail extends BaseModel {
    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "STAFFDETAIL_ID")
    private String staffdetail_id;

    @Column(name = "AWARD_ID")
    private String award_id;

    @Column(name = "ATTF")
    private String attf;

    @Column(name = "BUDGETCODE")
    private String budgetcode;

    @Column(name = "DEPART")
    private String depart;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
