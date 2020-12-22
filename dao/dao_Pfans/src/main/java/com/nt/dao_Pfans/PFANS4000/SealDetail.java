package com.nt.dao_Pfans.PFANS4000;

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
@Table(name = "sealdetail")
public class SealDetail extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "SEALDETAILID")
    private String sealdetailid;


    @Column(name = "SEALDETAILNAME")
    private String sealdetailname;


    @Column(name = "SEALDETAILDATE")
    private String sealdetaildate;
}
