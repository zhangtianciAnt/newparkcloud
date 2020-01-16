package com.nt.dao_Pfans.PFANS5000;

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
@Table(name = "outsource")
public class OutSource extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OUTSOURCE_ID")
    private String outsource_id;

    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    @Column(name = "BPCOMPANY")
    private String bpcompany;

    @Column(name = "BPNAME")
    private String bpname;

    @Column(name = "RN")
    private String rn;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
