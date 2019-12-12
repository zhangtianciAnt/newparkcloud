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
@Table(name = "otherone")
public class OtherOne extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 其他1ID
     */
    @Id
    private String otherone_id;

    private String giving_id;
    private String department_id;
    private String user_id;
    private String sex;
    private String workdate;
    private Date reststart;
    private Date restend;
    private String attendance;
    private String other1;
    private String basedata;
    private Date startdate;
    private Date enddate;
    private String vacation;
    private String handsupport;
    private String type;
    private Integer rowindex;
    private String jobnumber;

}
