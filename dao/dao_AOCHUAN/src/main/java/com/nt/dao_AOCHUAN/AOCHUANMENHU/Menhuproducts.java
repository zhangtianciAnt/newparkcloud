package com.nt.dao_AOCHUAN.AOCHUANMENHU;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menhuproducts")

public class Menhuproducts extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "MENHUPRODUCTS_ID")
    private String menhuproducts_id;

    @Column(name = "PNAME")
    private String pname;


    @Column(name = "ENGLISHNAME")
    private String englishname;


    @Column(name = "CASNUM")
    private String casnum;


    @Column(name = "STAUSS")
    private String stauss;

    @Column(name = "DTITLE")
    private String dtitle;

    @Column(name = "XTITLE")
    private String xtitle;



}
