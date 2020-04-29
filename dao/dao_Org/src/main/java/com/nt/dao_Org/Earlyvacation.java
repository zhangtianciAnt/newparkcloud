package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "earlyvacation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Earlyvacation extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EARLYVACATION_ID")
    private String earlyvacation_id;

    @Column(name = "USERNAMES")
    private String usernames;

    @Column(name = "ANNUALLEAVE")
    private String annualleave;

    @Column(name = "MARRIAGELEAVE")
    private String marriageleave;

    @Column(name = "MATERNITYLEAVE")
    private String maternityleave;

    @Column(name = "FUNERALLEAVE")
    private String funeralleave;

    @Column(name = "COMPENSATORYLEAVE")
    private String compensatoryleave;



}
