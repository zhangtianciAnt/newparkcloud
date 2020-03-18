package com.nt.dao_Pfans.PFANS2000.Vo;

import com.nt.dao_Pfans.PFANS2000.Lunarbasic;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LunarAllVo {

    private Lunarbonus lunarbonus;

    private List<Lunardetail> lunardetail;

    private List<Lunarbasic> lunarbasic;
}
