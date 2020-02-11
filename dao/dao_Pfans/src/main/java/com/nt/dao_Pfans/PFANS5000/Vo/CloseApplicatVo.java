package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS5000.CloseApplicat;
import com.nt.dao_Pfans.PFANS5000.ProjectSecore;
import com.nt.dao_Pfans.PFANS5000.StageNews;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseApplicatVo {
    private CloseApplicat closeApplicat;

    private List<ProjectSecore> projectSecore;

    private List<StageNews> stageNews;
}
