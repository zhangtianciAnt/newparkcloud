package com.nt.dao_Pfans.PFANS2000.Vo;

import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Staffexitproce;
import com.nt.dao_Pfans.PFANS2000.Citation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffexitprocedureVo{
    /**
     * 离职申请
     */
    private Staffexitprocedure staffexitprocedure;

    private Staffexitproce staffexitproce;
    /**
     * 引継项目
     */
    private List<Citation> citation;


}
