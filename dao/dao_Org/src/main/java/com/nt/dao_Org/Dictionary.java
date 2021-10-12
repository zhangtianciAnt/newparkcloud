package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "dictionary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dictionary extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "VALUE1")
    private String value1;

    @Column(name = "VALUE2")
    private String value2;

    @Column(name = "VALUE3")
    private String value3;

    @Column(name = "VALUE4")
    private String value4;

    @Column(name = "VALUE5")
    private String value5;

    @Column(name = "VALUE6")
    private String value6;

    @Column(name = "VALUE7")
    private String value7;

    @Column(name = "VALUE8")
    private String value8;

    @Column(name = "VALUE9")
    private String value9;

    //人件费 需要添加 1228 ccm
    @Column(name = "VALUE10")
    private String value10;
    //人件费 需要添加 1228 ccm
    //人件费 需要添加 1229 ztc
    @Column(name = "VALUE11")
    private String value11;
    //人件费 需要添加 1229 ztc
    //人件费 需要添加 1228 ztc
    @Column(name = "VALUE12")
    private String value12;
    //人件费 需要添加 1229 ztc

    //事业计划 需要添加 1011 ccm
    @Column(name = "VALUE13")
    private String value13;
    //事业计划 需要添加 1011 ccm

    //事业计划 需要添加 1011 ccm
    @Column(name = "VALUE14")
    private String value14;
    //事业计划 需要添加 1011 ccm

    //事业计划 需要添加 1011 ccm
    @Column(name = "VALUE15")
    private String value15;
    //事业计划 需要添加 1011 ccm

    //事业计划 需要添加 1011 ccm
    @Column(name = "VALUE16")
    private String value16;
    //事业计划 需要添加 1011 ccm

    //事业计划 需要添加 1011 ccm
    @Column(name = "VALUE17")
    private String value17;
    //事业计划 需要添加 1011 ccm

    //事业计划 需要添加 1011 ccm
    @Column(name = "VALUE18")
    private String value18;
    //事业计划 需要添加 1011 ccm

    //事业计划 需要添加 1011 ccm
    @Column(name = "VALUE19")
    private String value19;
    //事业计划 需要添加 1011 ccm

    //事业计划 需要添加 1011 ccm
    @Column(name = "VALUE20")
    private String value20;
    //事业计划 需要添加 1011 ccm

    @Column(name = "PCODE")
    private String pcode;

    @Column(name = "ORDERDIC")
    private Integer orderdic;

}
