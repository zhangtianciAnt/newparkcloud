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
@Table(name="vehiclemanagement")
public class VehicleManagement extends BaseModel {
    @Id
    private String vehiclemanagementid;
    private String carnum;
    private String imei;
    private String ikey;
    private String flag;
}
