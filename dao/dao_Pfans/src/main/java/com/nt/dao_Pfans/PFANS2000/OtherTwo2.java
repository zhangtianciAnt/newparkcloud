package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "othertwo2")
public class OtherTwo2 extends BaseModel {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "OTHERTWO2_ID")
    private String othertwo2_id;

    @Column(name = "GIVING_ID")
    private String giving_id;

    @Column(name = "JOBNUMBER")
    private String jobnumber;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "MONEYS")
    private String moneys;

    @Column(name = "ROOTKNOT")
    private String rootknot;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
