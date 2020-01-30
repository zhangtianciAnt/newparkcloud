package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projectinfo")
public class Projectinfo extends BaseModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String companyid;

    private String projectid;

    private String projectname;

    private Date starttime;

    private Date endtime;

    private String projectdescription;

    private String remarks;

}
