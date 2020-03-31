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
@Table(name = "personnelpermissions")
public class PersonnelPermissions extends BaseModel {

    @Id
    private String permissionsid;

    /**
     * 内部人员
     */
    private String companystaff;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 外来人员
     */
    private String foreignworkers;

}
