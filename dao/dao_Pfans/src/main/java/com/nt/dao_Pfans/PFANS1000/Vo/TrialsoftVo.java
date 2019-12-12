package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Trialsoft;
import com.nt.dao_Pfans.PFANS1000.Trialsoftdetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrialsoftVo {

    private Trialsoft trialsoft;

    private List<Trialsoftdetail> trialsoftdetail;
}
