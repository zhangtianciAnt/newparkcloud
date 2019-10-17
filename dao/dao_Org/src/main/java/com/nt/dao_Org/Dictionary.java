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
public class Dictionary implements Serializable {

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

    @Column(name = "PCODE")
    private String pcode;

}
