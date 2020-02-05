package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deviceinfo")
public class Deviceinfo extends BaseModel {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 添加时指定，不可重复
     */
    private String deviceid;

    /**
     * 业务/时钟
     */
    private String devicetype;

    /**
     * 所属机柜编号
     */
    private String cabinetid;

    private String currentuser;

    private String companyid;

    /**
     * 备注
     */
    private String remarks;

}
