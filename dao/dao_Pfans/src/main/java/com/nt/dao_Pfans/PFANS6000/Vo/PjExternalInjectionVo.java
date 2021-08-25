package com.nt.dao_Pfans.PFANS6000.Vo;

import com.nt.dao_Pfans.PFANS6000.PjExternalInjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PjExternalInjectionVo {

    private String pjExternalInjection_id;

    private String themeinfor_id;

    private String group_id;

    private String years;

    private String themename;

    private String divide;

    private String toolsorgs;

    private String companyprojects_id;

    private String project_name;

    private String numbers;

    private String userid;

    private String name;

    private String company;

    private String april;

    private String may;

    private String june;

    private String july;

    private String august;

    private String september;

    private String october;

    private String november;

    private String december;

    private String january;

    private String february;

    private String march;

    private String money;

    private String total;

    private List<PjExternalInjection> pjExternalInjectionList;

    private List<PjExternalInjectionVo> pjExternalInjectionListVo;
}
