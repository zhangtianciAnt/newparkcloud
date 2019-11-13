package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="vehiclemanagement")
public class VehicleManagement extends BaseModel {
    /**
     *一号车状态
     */
    private String carstatus1;

    /**
     *二号车状态
     */
    private String carstatus2;

    /**
     *三号车状态
     */
    private String carstatus3;

    /**
     *四号车状态
     */
    private String carstatus4;

    /**
     *五号车状态
     */
    private String carstatus5;

    /**
     *六号车状态
     */
    private String carstatus6;

    /**
     *七号车状态
     */
    private String carstatus7;

    /**
     *八号车状态
     */
    private String carstatus8;
}
