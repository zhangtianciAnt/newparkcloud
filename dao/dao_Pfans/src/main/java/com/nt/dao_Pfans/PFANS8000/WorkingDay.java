package com.nt.dao_Pfans.PFANS8000;
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
@Table(name = "workingday")
public class WorkingDay  extends BaseModel{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "WORKINGDAY_ID")
    private String workingday_id;

    @Id
    @Column(name = "WORKINGDATE")
    private Date workingdate;

    @Id
    @Column(name = "TYPE")
    private String type;
}
