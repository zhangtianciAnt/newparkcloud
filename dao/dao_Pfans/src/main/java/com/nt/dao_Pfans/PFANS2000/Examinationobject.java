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
@Table(name = "examinationobject")
public class Examinationobject extends BaseModel {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "EXAMINATIONOBJECT_ID")
    private String examinationobject_id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "RN")
    private String rn;

    @Column(name = "RANKKINDS")
    private String rankkinds;


}
