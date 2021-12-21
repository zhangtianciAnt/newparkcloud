package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "securitydoortoken")
public class Securitydoortoken extends BaseModel {

    @Id
    private int id;

    /*
     * 门检token
     */
    private String token;
}
