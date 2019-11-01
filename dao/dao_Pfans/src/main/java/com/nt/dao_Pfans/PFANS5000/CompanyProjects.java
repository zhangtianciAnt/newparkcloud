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
@Table(name = "companyprojects")
public class CompanyProjects extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    @Column(name = "PROJECT_NAME")
    private String project_name;




}
