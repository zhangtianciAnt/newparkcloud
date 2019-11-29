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
@Table(name = "additional")
public class Additional extends BaseModel {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ADDITIONAL_ID")
    private String additional_id;
    @Column(name = "GIVING_ID")
    private String giving_id;
    @Column(name = "USER_ID")
    private String user_id;
    @Column(name = "CHILDRENEDUCATION")
    private String childreneducation;
    @Column(name = "HOUSING")
    private String housing;
    @Column(name = "RENT")
    private String rent;
    @Column(name = "SUPPORT")
    private String support;
    @Column(name = "EDUCATION")
    private String education;
    @Column(name = "TOTAL")
    private String total;
    @Column(name = "CONFIRMS")
    private String confirms;
    @Column(name = "ROWINDEX")
    private Integer rowindex;
    @Column(name = "JOBNUMBER")
    private String jobnumber;
}
