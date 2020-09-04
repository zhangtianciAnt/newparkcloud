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
     * recnum
     */
    private String recnum;

    /**
     * allname
     */
    private String allname;

    /**
     * name
     */
    private String name;

    /**
     * classname
     * class1:内部员工；
     * class2：临时访客；
     * class3：承包商；
     * class4:其他
     */
    private String classname;

}