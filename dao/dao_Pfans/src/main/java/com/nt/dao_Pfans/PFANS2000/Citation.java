package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "citation")
public class Citation extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CITATION_ID")
    private String citation_id;
    @Column(name = "STAFFEXITPROCEDURE_ID")
    private String staffexitprocedure_id;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "USER_ID")
    private String user_id;
    @Column(name = "REMARKS")
    private String remarks;
    @OrderBy("ASC")
    @Column(name = "ROWINDEX")
    private String rowindex;
}
