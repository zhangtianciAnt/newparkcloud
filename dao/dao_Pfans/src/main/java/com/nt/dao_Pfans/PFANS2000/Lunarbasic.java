package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "lunarbasic")
public class Lunarbasic extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LUNARBASIC_ID")
    private String lunarbasic_id;

    @Column(name = "LUNARBONUS_ID")
    private String lunarbonus_id;

    @Column(name = "INDEX1")
    private Integer index;

    @Column(name = "R5")
    private String r5;

    @Column(name = "R5RATE")
    private String r5rate;

    @Column(name = "R6")
    private String r6;

    @Column(name = "R6RATE")
    private String r6rate;

    @Column(name = "R81")
    private String r81;

    @Column(name = "R81RATE")
    private String r81rate;

    @Column(name = "R82")
    private String r82;

    @Column(name = "R82RATE")
    private String r82rate;

    @Column(name = "R83")
    private String r83;

    @Column(name = "R83RATE")
    private String r83rate;

    @Column(name = "TITLE1")
    private String title1;

    @Column(name = "TITLE2")
    private String title2;

    @Column(name = "CODE")
    private String code;
}
