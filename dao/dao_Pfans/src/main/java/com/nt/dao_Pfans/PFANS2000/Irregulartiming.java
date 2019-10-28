package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Irregulartiming extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    private String irregulartiming_id;


    private String user_id;


    private String job;


    private String jobnumber;


    private String reason;


}
