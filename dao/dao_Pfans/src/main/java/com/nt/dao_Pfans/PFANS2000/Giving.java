package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "giving")
public class Giving extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "GIVING_ID")
    private String giving_id;


    @Column(name = "GENERATIONDATE")
    private Date generationdate;


    @Column(name = "GENERATION")
    private String generation;

}
