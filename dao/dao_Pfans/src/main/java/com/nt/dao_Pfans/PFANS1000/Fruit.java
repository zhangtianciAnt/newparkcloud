package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "fruit")
public class Fruit extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "FRUIT_ID")
    private String fruitid;

    @Id
    @Column(name = "QUOTATION_ID")
    private String quotationid;

    @Column(name = "FRUITION")
    private String fruition;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
