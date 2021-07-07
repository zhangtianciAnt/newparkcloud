package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "themeplan")
public class ThemePlan extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "THEMEPLAN_ID")
    private String themeplan_id;
    //add_qhr_20210707  增加themeinfor_id字段
    @Column(name = "THEMEINFOR_ID")
    private String themeinfor_id;

    @Column(name = "CENTER_ID")
    private String center_id;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "PLANCOUNT")
    private String plancount;

    @Column(name = "TYPE")
    private String type;
}
