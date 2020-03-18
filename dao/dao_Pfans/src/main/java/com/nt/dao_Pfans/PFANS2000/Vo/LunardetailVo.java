package com.nt.dao_Pfans.PFANS2000.Vo;

import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LunardetailVo {
    /**
     * 月度賞与考課一览
     */
//    private Lunarbonus lunarbonus;

    private String lunarbonus_id;

    private String group_id;

    private Date evaluationday;

    private String subjectmon;

    private String subject;

    private String evaluatenum;

    private String user_id;

    /**
     * 考课对象id
     */
    private String examinationobject_id;


}
