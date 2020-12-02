package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThemePlanDetailVo {
    /**
     * theme detail
     */
    //private ThemePlanDetail themeplandetail;

    private String themeplandetail_id;

    private String themeplan_id;

    private String pthemeplandetail_id;

    private String center_id;

    private String group_id;

    private String year;


    private String themename;

    private String remarks;

    private String branch;

    private String kind;

    private String contracttype;

    private String currencytype;

    private String assignor;

    private String personnel4;

    private String amount4;

    private String personnel5;

    private String amount5;

    private String personnel6;

    private String amount6;

    private String personnel7;

    private String amount7;

    private String personnel8;

    private String amount8;

    private String personnel9;

    private String amount9;

    private String personnel10;

    private String amount10;

    private String personnel11;

    private String amount11;

    private String personnel12;

    private String amount12;

    private String personnel1;

    private String amount1;

    private String personnel2;

    private String amount2;

    private String personnel3;

    private String amount3;

    private String plancount;

    private String rowindex;

    private String rowid;

    private String status;

    private String type;

    private String customerinfor_id;

    private String supplierinfor_id;

    private String month;

    private String wpersonnel4;

    private String wpersonnel5;

    private String wpersonnel6;

    private String wpersonnel7;

    private String wpersonnel8;

    private String wpersonnel9;

    private String wpersonnel10;

    private String wpersonnel11;

    private String wpersonnel12;

    private String wpersonnel1;

    private String wpersonnel2;

    private String wpersonnel3;

    private String sumpersonnel1;

    private String sumwpersonnel1;

    private String sumamount1;

    private String sumpersonnel2;

    private String sumwpersonnel2;

    private String sumamount2;

    private String sumpersonnel3;

    private String sumwpersonnel3;

    private String sumamount3;

    private String sumpersonnel4;

    private String sumwpersonnel4;

    private String sumamount4;








    /**
     * theme plan detail list
     */
    private List<ThemePlanDetail> children;
}
