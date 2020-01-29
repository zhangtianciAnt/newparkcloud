package com.nt.dao_Assets.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assets4ShowVo implements Serializable {

    private String assets_id;
    private String filename;
    private String typeassets;
    private String principal;
    private String barcode;
    private String bartype;
    private String assetstatus;
    private String stockstatus;
}
