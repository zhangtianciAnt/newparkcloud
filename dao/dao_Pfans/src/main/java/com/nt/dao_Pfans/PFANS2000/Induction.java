package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "induction")
public class Induction extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    private String induction_id;
    private String giving_id;
    private String user_id;
    private String lastmouth;
    private String thismouth;
    private Date worddate;
    private Date startdate;
    private String attendance;
    private String trial;
    private String give;
    private String lunch;
    private String traffic;
    private String remarks;
    private Integer rowindex;
    private String jobnumber;




}
