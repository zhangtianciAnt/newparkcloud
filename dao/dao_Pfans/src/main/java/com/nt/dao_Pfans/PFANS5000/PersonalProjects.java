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
@Table(name = "personalprojects")
public class PersonalProjects extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "PERSONALPROJECTS_ID")
    private String personalprojects_id;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "PROJECT_ID")
    private String project_id;


    @Column(name = "PROJECT_NAME")
    private String project_name;




}
