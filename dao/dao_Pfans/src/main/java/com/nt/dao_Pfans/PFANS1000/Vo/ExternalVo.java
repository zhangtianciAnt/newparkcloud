package com.nt.dao_Pfans.PFANS1000.Vo;


import com.nt.utils.Encryption.Encryption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalVo {
    //id
   private String expatriatesinfor_id;
   //人名
   @Encryption
   private String name;
     //供应商
     @Encryption
    private String suppliername;
    //rn
    private String thisyear;

    private String entermouth;

    private String suppliernameid;

    private String unitprice;

}
