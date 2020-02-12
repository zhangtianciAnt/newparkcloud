package com.nt.dao_Pfans.PFANS5000;

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
@Table(name = "projectsecore")
public class ProjectSecore extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PROJECTSECORE_ID")
    private String projectsecoreid;

    @Column(name = "CLOSEAPPLICAT_ID")
    private String closeapplicatid;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "NAME")
    private String name;

    @Column(name = "COMMUNE")
    private String commune;

    @Column(name = "CROPRATE")
    private String croprate;

    @Column(name = "PJCROPRATE")
    private String pjcroprate;

    @Column(name = "DICROPRATE")
    private String dicroprate;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
