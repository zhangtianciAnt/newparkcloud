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
@Table(name = "outsidedetail")
public class Outsidedetail extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "OUTSIDEDETAIL_ID")
    private String outsidedetailid;

    @Id
    @Column(name = "OUTSIDE_ID")
    private String outsideid;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "RANK")
    private String rank;

    @Column(name = "MAILADDRESS")
    private String mailaddress;

    @Column(name = "REASON")
    private Date reason;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
