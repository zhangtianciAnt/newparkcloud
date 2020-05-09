package com.nt.dao_Pfans.PFANS6000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class bpSum2Vo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String GROUP_ID;

    private String SUPPLIERNAME;

    private String OPERATIONFORM;

    private String date;

    private String worktime;
}
