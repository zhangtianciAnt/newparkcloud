package com.nt.dao_Pfans.PFANS6000;

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
@Table(name = "expatriatesinfordetail")
public class ExpatriatesinforDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 外驻人员信息Group履历
     */
    @Id
    @Column(name = "EXPATRIATESINFORDETAIL_ID")
    private String expatriatesinfordetail_id;
    /**
     * 外驻人员信息
     */
    @Id
    @Column(name = "EXPATRIATESINFOR_ID")
    private String expatriatesinfor_id;

    /**
     *
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     *
     */
    @Column(name = "EXDATESTR")
    private Date exdatestr;

    /**
     *
     */
    @Column(name = "EXDATEEND")
    private Date exdateend;

}
