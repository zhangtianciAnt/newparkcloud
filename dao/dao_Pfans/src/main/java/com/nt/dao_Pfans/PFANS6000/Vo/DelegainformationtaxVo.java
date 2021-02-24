package com.nt.dao_Pfans.PFANS6000.Vo;

import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Delegainformationtax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelegainformationtaxVo {
    private List<DelegainformationVo> delegainformationVo;
    private List<Delegainformationtax> delegainformationtaxList;
    private List<Delegainformation> delegainformationList;
}
