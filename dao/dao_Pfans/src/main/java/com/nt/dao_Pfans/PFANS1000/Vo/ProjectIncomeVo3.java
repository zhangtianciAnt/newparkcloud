package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectIncomeVo3 {

    private List<ProjectIncomeVo1> projectincomevo1;

    private List<ProjectIncomeVo2> projectincomevo2;

    private List<ProjectIncomeVo4> projectincomevo4;

    private List<Map<String,String>> projectincomevo5;
}
