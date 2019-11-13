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
    private String CarStatus1;

    /**
     *二号车状态
     */
    private String CarStatus2;

    /**
     *三号车状态
     */
    private String CarStatus3;

    /**
     *四号车状态
     */
    private String CarStatus4;

    /**
     *五号车状态
     */
    private String CarStatus5;

    /**
     *六号车状态
     */
    private String CarStatus6;
}
