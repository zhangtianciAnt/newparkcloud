package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personalcost")
public class PersonalCost extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 人件费ID
	 */
    @Id
    @Column(name = "PERSONALCOSTID")
    private String personalcostid;

    /**
     * 人件费年度ID
     */
    @Column(name = "YEARSANTID")
    private String yearsantid;


    /**
     *CENTERID
     */
    @Column(name = "CENTERID ")
    private String centerid ;

    /**
     *GROUPID
     */
    @Column(name = "GROUPID ")
    private String groupid ;

    /**
     * 姓名id
     */
    @Column(name = "USERID")
    private String userid;

    /**
     * 姓名name
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 部门简称
     */
    @Column(name = "DEPARTSHORT")
    private String departshort;

    /**
     * 配付与否
     */
    @Column(name = "ALLOTMENT")
    private String allotment;

    /**
     * 新人入社预定月
     */
    @Column(name = "NEWPERSONALDATE")
    private Date newpersonaldate;

    /**
     * 升格前Rn
     */
    @Column(name = "EXRANK")
    private String exrank;

}
