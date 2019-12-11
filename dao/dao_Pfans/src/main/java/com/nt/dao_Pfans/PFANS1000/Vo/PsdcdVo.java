package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Psdcd;
import com.nt.dao_Pfans.PFANS1000.Psdcddetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsdcdVo {

    private Psdcd psdcd;

    private List<Psdcddetail> psdcddetail;




}
