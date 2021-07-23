package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectIncomeVo4 {
    private String groupid;

    private String groupname;

    private String encoding;

    //0:center   1:group
    private String flag;
}
