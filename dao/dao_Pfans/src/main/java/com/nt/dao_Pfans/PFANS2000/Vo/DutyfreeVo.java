package com.nt.dao_Pfans.PFANS2000.Vo;

import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.dao_Pfans.PFANS2000.Wages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DutyfreeVo {
    /**
     * 基数表
     */
    private String user_id;
    private String giving_id;
    /**
     * 工资表
     */
    private String january;
    private String february;
    private String march;
    private String april;
    private String may;
    private String june;
    private String july;
    private String august;
    private String september;
    private String october;
    private String november;
    private String december;
    private String cumulative;




}
