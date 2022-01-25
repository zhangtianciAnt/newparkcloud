package com.nt.dao_Pfans.PFANS1000.Vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NapalmVo {

    private String contractnumber;

    private String contracttype;

    private String group_id;

    private String custochinese;

    private String type;

    private List<String> owners;

    private Date deliveryfinshdate;

}
