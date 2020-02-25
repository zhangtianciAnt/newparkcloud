package com.nt.dao_Pfans.PFANS1000;


import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personnelplan")
public class PersonnelPlan extends BaseModel {
    private static final long serialVersionUID = 1L;
    @Id
    private String personnelplanid;
    //年份
    private int years;
    //种类
    private int type;
    //现实点人员
    private String employed;
    //新入职人员
    private String newentry;

}
