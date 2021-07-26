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
@Table(name = "personalcostyears")
public class PersonalCostYears extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 人件费年度主键
     */
    @Id
    @Column(name = "YEARSANTID")
    private String yearsantid;

    /**
     * 年度
     */
    @Column(name = "YEARS")
    private String years;


}
