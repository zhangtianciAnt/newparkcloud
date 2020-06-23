package com.nt.dao_AOCHUAN.AOCHUAN4000;

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
@Table(name = "marketproducts")

public class Marketproducts extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "MARKETPRODUCTS_ID")
    private String marketproducts_id;

    @Column(name = "PRODUCTS_ID")
    private String products_id;

    @Column(name = "MARKETYPE")
    private String marketype;

    @Column(name = "DMFINFO")
    private String dmfinfo;

    @Column(name = "FZMEDICINETIM")
    private Date fzmedicinetim;

    @Column(name = "DRUGPRICE")
    private String drugprice;


    @Column(name = "IMSINFO")
    private String imsinfo;

    @Column(name = "REFERENCEPRICE")
    private String referenceprice;

    @Column(name = "UNITPRICE")
    private String unitprice;

    @Column(name = "PATENTSITUATIONINFO")
    private String patentsituationinfo;



}
