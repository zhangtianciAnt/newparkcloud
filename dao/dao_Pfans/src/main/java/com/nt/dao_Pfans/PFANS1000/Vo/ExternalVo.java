package com.nt.dao_Pfans.PFANS1000.Vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalVo {
    //人id
   private String expname;
   //人名
   private String coopername;
     //供应商id
    private String suppliername;
    //供应商名称
    private String supchinese;

}
