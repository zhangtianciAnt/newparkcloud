package com.nt.dao_Pfans.PFANS4000.Vo;

import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.dao_Pfans.PFANS4000.SealDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SealVo {
    private List<Seal> seal;
    private List<SealDetail> sealdetail;
}
