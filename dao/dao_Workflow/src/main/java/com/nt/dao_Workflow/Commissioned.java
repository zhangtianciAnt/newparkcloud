package com.nt.dao_Workflow;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "commissioned")
public class Commissioned extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 委托人ID
	 */
	@Id
    @Column(name = "COMMISSIONEDID")
    private String commissionedid;

	/**
	 * 被委托人
	 */
    @Column(name = "FROMUSER")
    private String fromuser;

	/**
	 * 委托人
	 */
    @Column(name = "TOUSER")
    private String touser;

	/**
	 * 截止日期
	 */
    @Column(name = "DATE")
    private Date date;

}
