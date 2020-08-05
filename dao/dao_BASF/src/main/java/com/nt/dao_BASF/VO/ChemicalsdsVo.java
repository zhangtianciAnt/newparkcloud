package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Chemicalsds;
import com.nt.dao_BASF.Emergencyplan;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChemicalsdsVo extends BaseModel {
    private Chemicalsds chemicalsds;
    private String token;
}
