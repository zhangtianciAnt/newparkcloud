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
@Table(name = "examinationproject")
public class Examinationproject extends BaseModel {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "EXAMINATIONPROJECT_ID")
    private String examinationproject_id;

    @Column(name = "EXAMINATIONOBJECT_ID")
    private String examinationobject_id;

    @Column(name = "BIGPROJECT")
    private String bigproject;

    @Column(name = "SMALLPROJECT")
    private String smallproject;

    @Column(name = "STATE")
    private String state;

    @Column(name = "RATIO")
    private String ratio;

    @Column(name = "INDEX")
    private Integer index;


}
