package com.nt.dao_Pfans.PFANS2000.Vo;

import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LunardetailVo {
    /**
     * 月度賞与考課一览
     */
    private Lunarbonus lunarbonus;
    /**
     * 考课对象id
     */
    private String examinationobject_id;


}
