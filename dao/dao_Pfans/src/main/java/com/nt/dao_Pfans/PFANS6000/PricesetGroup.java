package com.nt.dao_Pfans.PFANS6000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pricesetgroup")
public class PricesetGroup extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @Column(name = "PRICESETGROUP_ID")
    private String pricesetgroup_id;

    /**
     *査定時間
     */
    @Column(name = "PD_DATE")
    private String pd_date;


}
